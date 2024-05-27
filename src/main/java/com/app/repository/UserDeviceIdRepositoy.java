package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.constant.ServiceConstant.ROLE;
import com.app.model.UserDeviceIdDO;
import java.util.List ;

public interface UserDeviceIdRepositoy extends JpaRepository<UserDeviceIdDO, Long>{

	List<UserDeviceIdDO> findByRole(ROLE admin);
	
	List<UserDeviceIdDO> findByRoleAndBloodGroup(ROLE attender,String bloodGroup);

	UserDeviceIdDO findByUserId(Long id);
}
