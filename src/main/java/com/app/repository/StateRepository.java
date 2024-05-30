package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.constant.ServiceConstant.ROLE;
import com.app.model.StateDO;
import com.app.model.UserDeviceIdDO;
import java.util.List ;

public interface StateRepository extends JpaRepository<StateDO, Long>{

	
}
