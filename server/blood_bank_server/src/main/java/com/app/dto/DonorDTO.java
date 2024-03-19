package com.app.dto;

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
}
