package com.app.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.app.constant.ServiceConstant.ADVERTISE_TYPE;
import com.app.constant.ServiceConstant.STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor 
@Entity
@Table (name = "Advertisement")
public class AdvertisementDO {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    private String file;
    
    @Enumerated(EnumType.STRING)
    private ADVERTISE_TYPE type ;
    
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.ACTIVE;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDate updatedAt;
}
