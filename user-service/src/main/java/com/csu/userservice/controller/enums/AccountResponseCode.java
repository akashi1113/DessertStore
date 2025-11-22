package com.csu.userservice.enums;

public enum AccountResponseCode {
    // 通用状态码
    SUCCESS(200, "操作成功"),
    SERVER_ERROR(500, "服务器错误"),

    // 认证相关
    AUTH_SUCCESS(200, "登录成功"),
    AUTH_FAILED(401, "登录失败"),

    // 注册相关
    REGISTER_SUCCESS(200, "注册成功"),
    USER_NOT_FOUND(404, "用户不存在"),
    CODE_SENT(200, "验证码发送成功"),
    INVALID_VERIFICATION_CODE(400, "验证码无效"),

    // 登出相关
    LOGOUT_SUCCESS(200, "退出登录成功"),
    LOGOUT_FAILED(500, "退出登录失败"),

    // 查看/修改用户信息
    GET_SUCCESS(200, "请求成功"),
    PERMISSION_DENIED(403, "无权访问该用户信息"),
    UPDATE_SUCCESS(200, "用户信息更新成功"),
    PARAM_INVALID(400, "参数校验失败"),

    // 重置邮箱或密码
    EMAIL_RESET_SUCCESS(200, "邮箱重置成功"),
    PASSWORD_RESET_SUCCESS(200, "密码重置成功"),
    PASSWORD_INVALID(400, "无效密码"),
    EMAIL_ALREADY_EXISTS(409, "邮箱已被占用");

    private final int code;
    private final String message;

    AccountResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
