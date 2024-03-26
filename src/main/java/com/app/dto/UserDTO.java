package com.app.dto;

import java.time.LocalDateTime;
import java.util.Date;

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
    private String  city;    
    private STATUS status;
    private ROLE role;
    private String token;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
