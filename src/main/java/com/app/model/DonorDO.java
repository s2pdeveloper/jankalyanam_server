package com.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.GENDER;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table (name = "Donation")
public class DonorDO {

//    public DonorDO() {
//        this.bloodRequest = null; // Initializing address with null by default
//    }
	

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private GENDER gender;
    
    private String name;
    
    private int age;
    
    private String  state;
    
    private String  district;
    
    private String  tahsil;
    
    private String  village;
    
    @Column(name = "mobile_no")
    private String mobileNo;
    
    private double hemoglobin;

    private Boolean illness;
    
    @Column(name = "blood_group")
    private String bloodGroup;
    
    @Enumerated(EnumType.STRING)
    private DONOR_STATUS status = DONOR_STATUS.PENDING;
    
    private String location;
    
    @Column(name = "donation_date")
    private LocalDate donationDate;
    
    @Column(name = "blood_bank_name")
    private String bloodBankName;

    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "is_website")
    private Boolean isWebsite = false;
    
    private String  image;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToOne(mappedBy="donor")
    private BloodRequestDO bloodRequest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id", insertable = false, updatable = false)
    private UserDO user;
    
}
