package com.app.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    private int age;
    
    private String state;

    private String city;
    
    @Column(name = "mobile_no")
    private String mobileNo;
    
    private double hemoglobin;

    private Boolean illness;
    
    @Column(name = "blood_group")
    private String bloodGroup;
    
    @Column(name = "donation_date")
    private Date donationDate;

    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    
    @OneToOne(mappedBy="donor")
    private BloodRequestDO bloodRequest;
    
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserDO donor;
    
}
