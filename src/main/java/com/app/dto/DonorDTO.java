package com.app.dto;

import java.util.Date;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.GENDER;
import com.app.model.BloodRequestDO;
import com.app.model.UserDO;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class DonorDTO {
    private Long id;
    
    private String name;
    
    private int age;
    
    private String state;

    private String city;
    
    private String mobileNo;
    
    private double hemoglobin;

    private Boolean illness;
    
    private String bloodGroup;
    
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date donationDate;
    
    private GENDER gender;
    
    private DONOR_STATUS status;
    
    private String location;
    
    private String BloodBankName;
    
    private BloodDTO bloodRequest;
    
    private UserDTO user;
}
