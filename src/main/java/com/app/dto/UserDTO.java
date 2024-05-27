package com.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String  email;
    private Long  mobileNo;
    private String  state;
    private String  district; 
    private String  tahsil;
    private String  village;
    private Long  pincode;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate  DOB;
    private String bloodGroup;
    private STATUS status;
    private ROLE role;
    private String token;
    private String image;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime createdAt;
}
