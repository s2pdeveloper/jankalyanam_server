package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.app.constant.ServiceConstant.ROLE;
import com.app.model.DistrictDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.UserDeviceIdDO;
import com.app.model.VillageDO;

import java.util.List ;

public interface VillageRepository extends JpaRepository<VillageDO, Long>,JpaSpecificationExecutor<VillageDO>{

	List<VillageDO> findByTahsilId(Long tahsilId);

	
}
