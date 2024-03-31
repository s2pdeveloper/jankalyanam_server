package com.app.model;

import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor 
@Data
@Entity
@Table (name = "UserDevice")
public class UserDeviceIdDO {

	    @Id
		@GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	   
	    @Column(name = "device_id")
	   	private String deviceId;
	    
	    @Column(name = "user_id")
	   	private Long userId;
	    
	    @Column(name = "role")
	    @Enumerated(EnumType.STRING)
	    private ROLE role ;

	    
	    @OneToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
	    private UserDO userDetail;


}
