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

import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.BLOOD_TYPE;
import com.app.constant.ServiceConstant.GENDER;
import com.app.constant.ServiceConstant.PROVIDED;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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
    
    @Enumerated(EnumType.STRING)
    private GENDER gender;
    
    private int age;
    
    private String  state;
    
    private String  district;
    
    private String  tahsil;
    
    private String  village;
    
    private double hemoglobin;

    private String illness;
    
    @Column(name = "hospital_name")
    private String hospitalName;
    
    @Column(name = "mobile_no")
    private Long mobileNo;
    
    @Column(name = "blood_group")
    private String bloodGroup;
    
    private int units;
    
    @Column(name = "blood_require_date")
    private LocalDate bloodRequireDate;

    @Enumerated(EnumType.STRING)
    private BLOOD_STATUS status = BLOOD_STATUS.PENDING;
    
    @Column(name = "blood_type")
    @Enumerated(EnumType.STRING)
    private BLOOD_TYPE bloodType = BLOOD_TYPE.REGULAR;
    
    @Enumerated(EnumType.STRING)
    private PROVIDED provided;
    
    @Column(name = "blood_bank_name")
    private String bloodBankName;
    
    @Column(name = "bank_state")
    private String bankState;
    
    @Column(name = "bank_city")
    private String bankCity;
    
    @Column(name = "is_website")
    private Boolean isWebsite = false;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Column(name = "acceptor_id",updatable = true)
    private Long acceptorId;
    
    @Column(name = "attender_id")
    private Long attenderId;
    
    @Column(name = "donor_id",updatable = true)
    private Long donorId;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acceptor_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserDO acceptor;
  
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attender_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserDO attender;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", referencedColumnName = "id", insertable = false, updatable = false)
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
