package com.app.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.model.UserDO;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Long>{

	UserDO findByEmail(String email);
	
	UserDO findByMobileNo(String  mobileNo);
	
	List<UserDO> findByRoleAndStatus(ROLE role,STATUS status);



}
