package com.app.dto;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import com.app.constant.ServiceConstant.GENDER;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class DonorReponseDTO {
  
    private String name;
    private int age;
    private String state;
    private String city;
    private String mobileNo;
    private double hemoglobin;
    private Boolean illness;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate donationDate;
    private GENDER gender;
    private String bloodGroup;
    private String image;
	private Boolean isWebsite;
}
