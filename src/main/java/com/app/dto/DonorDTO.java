package com.app.dto;

import java.time.LocalDate;
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
    
    private String  state;
    
    private String  district;
    
    private String  tahsil;
    
    private String  village;
    
    private String mobileNo;
    
    private double hemoglobin;

    private Boolean illness;
    
    private String bloodGroup;
    
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate donationDate;
    
    private GENDER gender;
    
    private DONOR_STATUS status;
    
    private String location;
    
    private String bloodBankName;
    
    private String  image;
    
    private BloodResponseDTO bloodRequest;
    
    private UserDTO user;
    
}
