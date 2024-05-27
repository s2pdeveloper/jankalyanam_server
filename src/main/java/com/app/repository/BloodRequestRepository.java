package com.app.repository;
import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.dto.*;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.BloodRequestDO;



@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequestDO, Long> ,JpaSpecificationExecutor<BloodRequestDO>{
	Slice <BloodRequestDO> findByStatus(BLOOD_STATUS status,Pageable pageable);
	
	
    @Query("SELECT b FROM BloodRequestDO b WHERE b.status IN :status " +
            "AND (:search IS NULL OR :search = '' OR " +
            "(b.name LIKE %:search% OR " +
            "b.fatherOrHusband LIKE %:search% OR " +
            "b.gender LIKE %:search% OR " +
            "b.state LIKE %:search% OR " +
            "b.district LIKE %:search% OR " +
            "b.tahsil LIKE %:search% OR " +
            "b.village LIKE %:search% OR " +
            "b.illness LIKE %:search% OR " +
            "b.hospitalName LIKE %:search% OR " +
            "b.bloodType LIKE %:search% OR " +
            "b.bloodGroup LIKE %:search%))")
    Page<BloodRequestDO> findAllByStatus(List<BLOOD_STATUS> status,String search,Pageable pageable);
	


    @Query("SELECT b FROM BloodRequestDO b WHERE b.status IN :status AND b.acceptorId = :id " +
            "AND (:search IS NULL OR :search = '' OR " +
            "(b.name LIKE %:search% OR " +
            "b.fatherOrHusband LIKE %:search% OR " +
            "b.gender LIKE %:search% OR " +
            "b.state LIKE %:search% OR " +
            "b.district LIKE %:search% OR " +
            "b.tahsil LIKE %:search% OR " +
            "b.village LIKE %:search% OR " +
            "b.illness LIKE %:search% OR " +
            "b.hospitalName LIKE %:search% OR " +
            "b.bloodType LIKE %:search% OR " +
            "b.bloodGroup LIKE %:search%))")
    Page <BloodRequestDO> findByStatusAndAdminId(List<BLOOD_STATUS> status,Long id, String search,Pageable pageable);
	
	
	@Query("SELECT b FROM BloodRequestDO b WHERE b.status in :status and b.attenderId = :id " +
            "AND (:search IS NULL OR :search = '' OR " +
            "(b.name LIKE %:search% OR " +
            "b.fatherOrHusband LIKE %:search% OR " +
            "b.gender LIKE %:search% OR " +
            "b.state LIKE %:search% OR " +
            "b.district LIKE %:search% OR " +
            "b.tahsil LIKE %:search% OR " +
            "b.village LIKE %:search% OR " +
            "b.illness LIKE %:search% OR " +
            "b.hospitalName LIKE %:search% OR " +
            "b.bloodType LIKE %:search% OR " +
            "b.bloodGroup LIKE %:search%))")
	Page <BloodRequestDO> findByStatusInAndAttenderId(List<BLOOD_STATUS> status,Long id,String search,Pageable pageable);

	
//	Page <BloodRequestDO> findAllRequest(String status,Date startDate,Date endDate,String search,Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("UPDATE BloodRequestDO b SET b.status = ?2 where b.id = ?1")
	void findByIdAndUpdateStatus(Long id,BLOOD_STATUS status);
	
	@Modifying
	@Transactional
	@Query("UPDATE BloodRequestDO b SET b.status = :status ,b.acceptorId = :acceptorId where b.id = :id")
	void findAndChangeAdmin(Long id,BLOOD_STATUS status,Long acceptorId);


}
