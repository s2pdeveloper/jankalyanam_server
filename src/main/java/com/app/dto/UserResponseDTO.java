package com.app.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.OneToMany;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class UserResponseDTO {
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
    private List<BloodDTO> myList;
    private List<BloodDTO> bloodRequestList;
    private List<DonorRequestDTO> bloodDonateList ;
}
