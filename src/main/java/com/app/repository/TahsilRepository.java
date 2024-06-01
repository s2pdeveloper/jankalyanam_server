package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.app.constant.ServiceConstant.ROLE;
import com.app.model.BloodRequestDO;
import com.app.model.DistrictDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.UserDeviceIdDO;
import java.util.List ;

public interface TahsilRepository extends JpaRepository<TahsilDO, Long> ,JpaSpecificationExecutor<TahsilDO>{

	List<TahsilDO> findByDistrictId(Long districtId);

	
}
