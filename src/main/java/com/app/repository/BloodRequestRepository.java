package com.app.repository;
import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.dto.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.BloodRequestDO;



@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequestDO, Long> {
	Page <BloodRequestDO> findByStatus(BLOOD_STATUS status,Pageable pageable);
	
	
	Page<BloodRequestDO> findByStatusIn(List<BLOOD_STATUS> status,Pageable pageable);
	

	@Query("SELECT b FROM BloodRequestDO b WHERE b.status in ?1 and b.acceptorId = ?2")
	Page <BloodRequestDO> findByStatusAndAdminId(List<BLOOD_STATUS> status,Long id,Pageable pageable);
	
	
	@Query("SELECT b FROM BloodRequestDO b WHERE b.status in ?1 and b.attenderId = ?2")
	List <BloodRequestDO> findByStatusAndAttenderId(List<BLOOD_STATUS> status,Long id);

	@Modifying
	@Transactional
	@Query("UPDATE BloodRequestDO b SET b.status = ?2 where b.id = ?1")
	void findByIdAndUpdateStatus(Long id,BLOOD_STATUS status);
}
