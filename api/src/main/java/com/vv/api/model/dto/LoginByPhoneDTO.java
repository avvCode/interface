package com.vv.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginByPhoneDTO {
    private String userEmail;
    private String smsCode;
}
