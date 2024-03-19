package com.app.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor 
@Getter
@Setter
@Entity
@Table (name = "BloodRequest")
public class BloodRequestDO {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @Column(name = "father_or_husband")
    private String fatherOrHusband;
    
    private int age;
    
    private String state;

    private String city;
    
    private double hemoglobin;

    private String illness;
    
    private String location;
    
    @Column(name = "mobile_no")
    private Long mobileNo;
    
    @Column(name = "blood_group")
    private String bloodGroup;
    
    private int units;
    
    @Column(name = "blood_require_date")
    private Date bloodRequireDate;

    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    
  
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserDO user;
    
    @OneToOne
    @JoinColumn(name = "donor_id")
    private DonorDO donor;
    
    
//    @JsonManagedReference
//    
//    @ManyToOne()
//    @JsonBackReference
//    @JoinColumn(name = "user_id")
//    private User user;
//    @JsonIgnoreProperties("bloodRequestList")
//    @JsonManagedReference
    
    


}
