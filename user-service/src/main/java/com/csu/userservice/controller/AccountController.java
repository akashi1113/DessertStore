package com.csu.userservice.controller;

import com.csu.userservice.config.JwtUtil;
import com.csu.userservice.dto.*;
import com.csu.userservice.entity.Account;
import com.csu.userservice.enums.AccountResponseCode;
import com.csu.userservice.service.AccountService;
import com.csu.userservice.service.NewImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    // ===========================
    // 用户注册（邮箱注册）
    // ===========================
    @PostMapping
    public ResponseEntity<AccountResponse<?>> signup(
            @RequestParam String email,
            @RequestParam String username,
            @RequestBody @Valid PasswordRequest request,
            BindingResult bindingResult) {

        // 参数校验
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getFieldErrors().stream()
                    .findFirst()
                    .map(FieldError::getDefaultMessage)
                    .orElse("请求参数无效");
            return ResponseEntity.badRequest()
                    .body(AccountResponse.error(AccountResponseCode.PASSWORD_INVALID, msg));
        }

        // 检查邮箱是否重复
        if (accountService.getAccountByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AccountResponse.errors(AccountResponseCode.EMAIL_ALREADY_EXISTS, null));
        }

        // 插入
        accountService.insertEmailAccount(email, username, request.getPassword());
        Account account = accountService.getAccountByEmail(email);

        // 生成 token
        String jwt = jwtUtil.generateToken(
                account.getUserid(),
                account.getUsername(),
                account.getTokenVersion()
        );

        // 构造响应
        AccountResponse.BasicResponse data = new AccountResponse.BasicResponse();
        data.setUserid(account.getUserid());
        data.setUsername(account.getUsername());
        data.setEmail(account.getEmail());
        data.setToken(jwt);

        accountService.setLoginType(account.getUserid(), "email");

        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.REGISTER_SUCCESS, data));
    }

    // ===========================
    // 获取用户 ID（根据 token）
    // ===========================
    @GetMapping
    public ResponseEntity<AccountResponse<?>> getUserId(
            @RequestHeader("Authorization") String token) {

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.SUCCESS, userId));
    }

    // ===========================
    // 获取用户详细信息
    // ===========================
    @GetMapping("/{userid}")
    public ResponseEntity<AccountResponse<AccountResponse.DetailResponse>> getAccount(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid) {

        Account account = accountService.getAccountById(userid);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND, null));
        }

        // 权限校验
        if (!jwtUtil.validateToken(
                token.replace("Bearer ", ""),
                userid,
                account.getUsername(),
                account.getTokenVersion())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        // 返回详细信息
        AccountResponse.DetailResponse data = new AccountResponse.DetailResponse();
        data.setUserid(account.getUserid());
        data.setUsername(account.getUsername());
        data.setEmail(account.getEmail());
        data.setPhone(account.getPhone());
        data.setGender(account.getGender());
        data.setAge(account.getAge());
        data.setAddr1(account.getAddr1());
        data.setAddr2(account.getAddr2());
        data.setVIPLevel(account.getVIPLevel());
        // ✅ 这里改成实体的 getAvatarUrl()
        data.setAvatar_url(account.getAvatarUrl());

        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.GET_SUCCESS, data));
    }

    // ===========================
    // 修改个人资料
    // ===========================
    @PutMapping("/{userid}")
    public ResponseEntity<AccountResponse<?>> updateAccount(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestBody @Valid AccountUpdateRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(AccountResponse.errors(AccountResponseCode.PARAM_INVALID, bindingResult));
        }

        Account account = accountService.getAccountById(userid);

        // 权限校验
        if (!jwtUtil.validateToken(
                token.replace("Bearer ", ""),
                userid,
                account.getUsername(),
                account.getTokenVersion())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        int result = accountService.updateAccount(userid, request);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND, null));
        }
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.UPDATE_SUCCESS, null));
    }

    // ===========================
    // 上传头像
    // ===========================
    @PostMapping("/{userid}/avatar")
    public ResponseEntity<AccountResponse<?>> uploadAvatar(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestParam("file") MultipartFile file) throws IOException {

        Account account = accountService.getAccountById(userid);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND, null));
        }

        // 权限校验
        if (!jwtUtil.validateToken(
                token.replace("Bearer ", ""),
                userid,
                account.getUsername(),
                account.getTokenVersion())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        // 上传文件至 OSS
        String url = NewImageService.uploadImage(file.getBytes());
        accountService.setAvatar(userid, url);

        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.UPDATE_SUCCESS, null));
    }

    // ===========================
    // 修改邮箱
    // ===========================
    @PostMapping("/{userid}/email")
    public ResponseEntity<AccountResponse<?>> resetEmail(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestParam @Email String newEmail) {

        if (accountService.getAccountByEmail(newEmail) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AccountResponse.errors(AccountResponseCode.EMAIL_ALREADY_EXISTS, null));
        }

        Account account = accountService.getAccountById(userid);

        // 权限校验
        if (!jwtUtil.validateToken(
                token.replace("Bearer ", ""),
                userid,
                account.getUsername(),
                account.getTokenVersion())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        accountService.resetEmail(userid, newEmail);
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.EMAIL_RESET_SUCCESS, newEmail));
    }

    // ===========================
    // 修改密码
    // ===========================
    @PostMapping("/{userid}/password")
    public ResponseEntity<AccountResponse<?>> resetPassword(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestBody @Valid PasswordRequest request,
            BindingResult bindingResult) {

        // 参数校验
        if (bindingResult.hasErrors()) {
            String msg = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse("请求参数无效");

            return ResponseEntity.badRequest()
                    .body(AccountResponse.error(AccountResponseCode.PASSWORD_INVALID, msg));
        }

        Account account = accountService.getAccountById(userid);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND, null));
        }

        // 权限校验
        if (!jwtUtil.validateToken(
                token.replace("Bearer ", ""),
                userid,
                account.getUsername(),
                account.getTokenVersion())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        // 执行
        accountService.resetPassword(userid, request.getPassword());
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.PASSWORD_RESET_SUCCESS, null));
    }
}
