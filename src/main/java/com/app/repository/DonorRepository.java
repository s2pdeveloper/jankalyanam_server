package com.app.repository;



import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.model.DonorDO;

@Repository
public interface DonorRepository extends JpaRepository<DonorDO, Long>{

	List <DonorDO> findByStatus(DONOR_STATUS status);
	
	@Query("SELECT b FROM DonorDO b WHERE b.status IN :status" +
            "AND (:search IS NULL OR :search = '' OR " +
            "(b.name LIKE %:search% OR " +
            "b.gender LIKE %:search% OR " +
            "b.state LIKE %:search% OR " +
            "b.city LIKE %:search% OR " +
            "b.mobileNo LIKE %:search% OR " +
            "b.bloodGroup LIKE %:search% OR " +
            "b.location LIKE %:search%))")
	Slice<DonorDO> findByStatusIn(List<DONOR_STATUS> status,String search,Pageable pageable);
	
	

	@Query("SELECT b FROM DonorDO b WHERE b.status IN :status AND b.userId = :id " +
	            "AND (:search IS NULL OR :search = '' OR " +
	            "(b.name LIKE %:search% OR " +
	            "b.gender LIKE %:search% OR " +
	            "b.state LIKE %:search% OR " +
	            "b.city LIKE %:search% OR " +
	            "b.mobileNo LIKE %:search% OR " +
	            "b.bloodGroup LIKE %:search% OR " +
	            "b.location LIKE %:search%))")
	Slice <DonorDO> findByStatusAndAttenderId(List<DONOR_STATUS> status,Long id,String search,Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE DonorDO b SET b.status = ?2 where b.id = ?1")
	void findByIdAndUpdateStatus(Long id,DONOR_STATUS status);

	Slice<DonorDO> findByBloodGroupAndStatusIn(List<DONOR_STATUS> status, Pageable paging);

}
