package com.csu.userservice.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class AccountUpdateRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 20, message = "用户名长度需 1-20 字符")
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Min(value = 1, message = "年龄最小为 1 岁")
    @Max(value = 150, message = "年龄最大为 150 岁")
    private int age;

    private int gender;

    private String addr1;
    private String addr2;
    private String avatar_url;
}
