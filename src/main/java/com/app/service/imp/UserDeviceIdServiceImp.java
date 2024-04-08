package com.app.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.* ;
import java.util.stream.Collectors;

import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.AdminsDeviceDTO;
import com.app.dto.ResultDTO;
import com.app.model.UserDeviceIdDO;
import com.app.repository.UserDeviceIdRepositoy;
import com.app.repository.UserRepository;
import com.app.service.UserDeviceIdService;
import com.app.utilities.Utility;

@Service
public class UserDeviceIdServiceImp implements UserDeviceIdService {

	@Autowired
	private UserDeviceIdRepositoy userDeviceRepository ;
	
	

	
	@Override
	public  ResultDTO save(String deviceId) {
		UserDeviceIdDO  userDeviceData = userDeviceRepository.findByUserId(Utility.getSessionUser().getId());
		if(userDeviceData == null) {
			UserDeviceIdDO userDevice =  new UserDeviceIdDO();
			userDevice.setDeviceId(deviceId);
			userDevice.setUserId(Utility.getSessionUser().getId());
			userDevice.setRole(Utility.getSessionUser().getRole());
			userDeviceRepository.save(userDevice) ;
		}else {
			userDeviceData.setDeviceId(deviceId);
			userDeviceRepository.save(userDeviceData) ;
		}
	
		return new ResultDTO("","Save Successfully!");
	}
	
	@Override
	public List<String> getAdminsAndDeviceId(){
		 List<UserDeviceIdDO> admins = userDeviceRepository.findByRole(ROLE.ADMIN);
	      if(admins.size()  == 0){
	    	  return new ArrayList<>();
	      }
	     return admins.stream()
	    		    .map(UserDeviceIdDO::getDeviceId) 
	    		    .filter(Objects::nonNull) 
	    		    .collect(Collectors.toList());
	     
	    }
		
	@Override
	public String getDeviceId(Long id){
		UserDeviceIdDO userDevice = userDeviceRepository.findById(id).orElse(null);
	      return userDevice != null ? userDevice.getDeviceId() : null;
	    }

}
