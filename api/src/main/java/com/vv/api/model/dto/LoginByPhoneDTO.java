package com.vv.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginByPhoneDTO implements Serializable {
    private String userPhone;
    private String smsCode;
}
