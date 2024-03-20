package com.app.repository;
import com.app.dto.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.BloodRequestDO;



@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequestDO, Long>{
	List <BloodRequestDO> findByStatus(String status);
	
	
	List<BloodRequestDO> findByStatusIn(List<String> status);
	
//	@Query('')
	List <BloodRequestDO> findStatusesByUserId(Long userId);

}
