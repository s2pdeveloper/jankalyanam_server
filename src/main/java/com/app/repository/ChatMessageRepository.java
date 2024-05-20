package com.app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.constant.ServiceConstant.STATUS;
import com.app.model.AdvertisementDO;
import com.app.model.ChatMessageDO;

import java.util.List;
import ch.qos.logback.core.status.Status;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageDO, Long>{

	List<ChatMessageDO> findByRoomOrderBycreatedAtAsc(String room);

}
