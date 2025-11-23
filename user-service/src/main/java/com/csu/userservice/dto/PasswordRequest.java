package com.csu.userservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequest {

    @NotBlank(message = "新密码不能为空")
    @Size(min = 4, max = 20, message = "密码长度需 4-20 位")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "密码需包含大小写字母和数字")
    private String password;
}
