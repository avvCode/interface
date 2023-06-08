package com.vv.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginByEmailDTO {
    private String userEmail;
    private String password;
    private String confirmPassword;
}
