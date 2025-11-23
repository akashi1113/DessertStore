package com.csu.userservice.dto;

import com.csu.userservice.enums.AccountResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AccountResponse<T> {

    private int code;
    private String message;
    private T data;
    private Map<String, String> errors;

    public AccountResponse() {}

    public AccountResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功响应 */
    public static <T> AccountResponse<T> success(AccountResponseCode code, T data) {
        return new AccountResponse<>(code.getCode(), code.getMessage(), data);
    }

    /** 校验错误响应 */
    public static <T> AccountResponse<T> errors(AccountResponseCode code, BindingResult bindingResult) {
        AccountResponse<T> response = new AccountResponse<>();
        response.setCode(code.getCode());
        response.setMessage(code.getMessage());

        if (bindingResult != null) {
            response.setErrors(
                    bindingResult.getFieldErrors().stream()
                            .collect(Collectors.toMap(
                                    FieldError::getField,
                                    FieldError::getDefaultMessage
                            ))
            );
        }

        return response;
    }

    /** 单条错误消息 */
    public static <T> AccountResponse<T> error(AccountResponseCode code, String errorMessage) {
        AccountResponse<T> response = new AccountResponse<>();
        response.setCode(code.getCode());
        response.setMessage(code.getMessage());
        response.setData((T) errorMessage);
        return response;
    }

    /** ============ 用户基础信息 ============ */
    @Data
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class BasicResponse {
        private String token;
        private Long userid;
        private String username;
        private String email;
        private String login_type;
    }

    /** ============ 用户完整信息 ============ */
    @Data
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class DetailResponse {
        private Long userid;
        private String username;
        private String email;
        private String phone;
        private int gender;
        private int age;
        private String addr1;
        private String addr2;
        private int VIPLevel;
        private String avatar_url;
        private String login_type;
    }
}
