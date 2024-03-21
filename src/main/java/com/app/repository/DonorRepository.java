package com.app.repository;



import java.util.List;

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
	
	
	List<DonorDO> findByStatusIn(List<DONOR_STATUS> status);
	
	
	@Query("SELECT b FROM DonorDO b WHERE b.status in ?1 and b.userId = ?2")
	List <DonorDO> findByStatusAndAttenderId(List<DONOR_STATUS> status,Long id);

	@Modifying
	@Transactional
	@Query("UPDATE DonorDO b SET b.status = ?2 where b.id = ?1")
	void findByIdAndUpdateStatus(Long id,DONOR_STATUS status);

}
