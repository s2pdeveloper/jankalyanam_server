package com.app.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.model.BloodRequestDO;
import com.app.model.DonorDO;

@Repository
public interface DonorRepository extends JpaRepository<DonorDO, Long>,JpaSpecificationExecutor<DonorDO>{

	List <DonorDO> findByStatus(DONOR_STATUS status);
	
	@Query("SELECT b FROM DonorDO b WHERE b.status IN :status " +
            "AND (:search IS NULL OR :search = '' OR " +
            "(b.name LIKE %:search% OR " +
            "b.gender LIKE %:search% OR " +
            "b.state LIKE %:search% OR " +
            "b.district LIKE %:search% OR " +
            "b.tahsil LIKE %:search% OR " +
            "b.village LIKE %:search% OR " +
            "b.mobileNo LIKE %:search% OR " +
            "b.bloodGroup LIKE %:search% OR " +
            "b.location LIKE %:search%))")
	Page<DonorDO> findByStatusIn(List<DONOR_STATUS> status,String search,Pageable pageable);
	
	

	@Query("SELECT b FROM DonorDO b WHERE b.status IN :status AND b.userId = :id " +
	            "AND (:search IS NULL OR :search = '' OR " +
	            "(b.name LIKE %:search% OR " +
	            "b.gender LIKE %:search% OR " +
	            "b.state LIKE %:search% OR " +
	            "b.district LIKE %:search% OR " +
	            "b.tahsil LIKE %:search% OR " +
	            "b.village LIKE %:search% OR " +
	            "b.mobileNo LIKE %:search% OR " +
	            "b.bloodGroup LIKE %:search% OR " +
	            "b.location LIKE %:search%))")
	Page <DonorDO> findByStatusAndAttenderId(List<DONOR_STATUS> status,Long id,String search,Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE DonorDO b SET b.status = ?2 where b.id = ?1")
	void findByIdAndUpdateStatus(Long id,DONOR_STATUS status);

	@Query("SELECT b FROM DonorDO b LEFT JOIN b.bloodRequest br WHERE b.bloodGroup = :group AND b.status IN :status AND br IS NULL")
    Page<DonorDO> findByBloodGroupAndStatusInAndBloodRequestIsNull(String group, List<DONOR_STATUS> status, Pageable paging);
}
