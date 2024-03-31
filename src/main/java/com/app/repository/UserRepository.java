package com.app.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.model.UserDO;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Long>{

	UserDO findByEmail(String email);
	
	UserDO findByMobileNo(String  mobileNo);
	
    @Query("SELECT b FROM UserDO b WHERE b.role = :role AND b.status = :status   " +
            "AND (:search IS NULL OR :search = '' OR " +
            "(b.firstName LIKE %:search% OR " +
            "b.lastName LIKE %:search% OR " +
            "b.mobileNo LIKE %:search% OR " +
            "b.state LIKE %:search% OR " +
            "b.city LIKE %:search% OR " +
            "b.address LIKE %:search%))")
	Page<UserDO> findByRoleAndStatus(ROLE role,STATUS status,Pageable pageable,String search);
	
	Boolean existsByMobileNo(String  mobileNo);

	Optional<UserDO> findByRole(ROLE superAdmin);



}
