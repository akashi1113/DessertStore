package com.csu.userservice.controller;

import com.csu.userservice.config.JwtUtil;
import com.csu.userservice.dto.*;
import com.csu.userservice.entity.Account;
import com.csu.userservice.enums.AccountResponseCode;
import com.csu.userservice.service.AccountService;
import com.csu.userservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    private final EmailService emailService = new EmailService();

    /** 发送邮箱验证码 */
    @PostMapping("/emailCode")
    public ResponseEntity<AccountResponse<?>> sendCode(
            @RequestParam String email,
            @RequestParam String username) {

        String code = emailService.sendEmail(email, username);
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.CODE_SENT, code));
    }

    /** 邮箱 + 密码登录 */
    @PostMapping("/email")
    public ResponseEntity<AccountResponse<?>> loginWithEmail(
            @RequestParam String email,
            @RequestBody PasswordRequest request) {

        try {
            Account account = accountService.login(email, request.getPassword());

            // 登录失败（账号或密码错误）
            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AccountResponse.errors(AccountResponseCode.AUTH_FAILED, null));
            }

            accountService.setLoginType(account.getUserid(), "email");

            // 生成 JWT
            String token = jwtUtil.generateToken(
                    account.getUserid(),
                    account.getUsername(),
                    account.getTokenVersion()
            );

            AccountResponse.BasicResponse data = new AccountResponse.BasicResponse();
            data.setUserid(account.getUserid());
            data.setEmail(account.getEmail());
            data.setUsername(account.getUsername());
            data.setToken(token);
            data.setLogin_type("email");

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(AccountResponse.success(AccountResponseCode.AUTH_SUCCESS, data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AccountResponse.errors(AccountResponseCode.SERVER_ERROR, null));
        }
    }

    /** 忘记密码 → 重置密码 */
    @PostMapping("/password")
    public ResponseEntity<AccountResponse<?>> resetPasswordByEmail(
            @RequestParam String email,
            @RequestBody @Valid PasswordRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse("请求参数无效");

            return ResponseEntity.badRequest()
                    .body(AccountResponse.error(AccountResponseCode.PASSWORD_INVALID, msg));
        }

        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND, null));
        }

        accountService.resetPassword(account.getUserid(), request.getPassword());
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.PASSWORD_RESET_SUCCESS, null));
    }

    /** GitHub 登录回调 */
    @GetMapping("/github")
    public ResponseEntity<AccountResponse<AccountResponse.BasicResponse>> handleOAuth2Callback(
            @RequestParam String token) {

        try {
            Long userid = jwtUtil.extractUserId(token);
            Account account = accountService.getAccountById(userid);

            AccountResponse.BasicResponse data = new AccountResponse.BasicResponse();
            data.setUserid(userid);
            data.setUsername(account.getUsername());
            data.setEmail(account.getEmail());
            data.setToken(token);
            data.setLogin_type("github");

            return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.AUTH_SUCCESS, data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AccountResponse.errors(AccountResponseCode.SERVER_ERROR, null));
        }
    }

    /** 退出登录 */
    @DeleteMapping
    public ResponseEntity<AccountResponse<?>> logout(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userid = jwtUtil.extractUserId(token);

        // token 失效（加入黑名单 + 版本号 +1）
        jwtUtil.invalidateToken(token);
        accountService.updateTokenVersion(userid);

        return ResponseEntity.ok(
                AccountResponse.success(AccountResponseCode.LOGOUT_SUCCESS, null)
        );
    }
}
