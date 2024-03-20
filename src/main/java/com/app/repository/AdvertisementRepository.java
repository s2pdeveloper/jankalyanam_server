package com.app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.AdvertisementDO;

@Repository
public interface AdvertisementRepository extends JpaRepository<AdvertisementDO, Long>{



}
