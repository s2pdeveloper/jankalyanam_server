package com.app.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table (name = "tahsil")
public class TahsilDO {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tahsil_name")
    private String tahsilName;
    
    @Column(name = "district_id")
    private Long districtId;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "tahsil_id", referencedColumnName = "id")
    private List<VillageDO> tahsilVillage;
}
