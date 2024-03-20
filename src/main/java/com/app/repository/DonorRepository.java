package com.app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.DonorDO;

@Repository
public interface DonorRepository extends JpaRepository<DonorDO, Long>{



}
