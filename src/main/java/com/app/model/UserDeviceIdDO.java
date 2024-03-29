package com.app.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class UserDeviceIdDO {

	    @Id
		@GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	   
	    @Column(name = "device_id")
	   	private String deviceId;
	    
	    @Column(name = "user_id")
	   	private String userId;
	    
	    @OneToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
	    private UserDO user;
}
