package com.app.dto;

import com.app.constant.ServiceConstant.ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String firstName;
    private String lastName;
    private String    mobileNo;
    private String  password;
    private ROLE role;
}
