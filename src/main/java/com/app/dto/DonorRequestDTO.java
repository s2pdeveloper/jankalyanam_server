package com.app.dto;

import java.util.Date;

import com.app.constant.ServiceConstant.BloodGroup;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.GENDER;
import com.app.model.BloodRequestDO;
import com.app.model.UserDO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class DonorRequestDTO {
    
    private String name;
    
    private int age;
    
    private String state;

    private String city;
    
    private String mobileNo;
    
    private double hemoglobin;

    private Boolean illness;
    
    private Date donationDate;

    private GENDER gender;
}
