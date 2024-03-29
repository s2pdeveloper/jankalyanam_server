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
		UserDeviceIdDO userDevice =  new UserDeviceIdDO();
		userDevice.setDeviceId(deviceId);
		userDevice.setUserId(Utility.getSessionUser().getId());
		userDevice.setRole(Utility.getSessionUser().getRole());
		UserDeviceIdDO UserDeviceSave = userDeviceRepository.save(userDevice) ;
		return new ResultDTO(UserDeviceSave.getId().toString(),"Save Successfully!");
	}
//	
//	
	@Override
	public List<AdminsDeviceDTO> getAdminsAndDeviceId(){
		 List<UserDeviceIdDO> admins = userDeviceRepository.findByRole(ROLE.ADMIN);
	        return admins.stream()
	        		.map((UserDeviceIdDO admin) -> new AdminsDeviceDTO(admin.getId(), admin.getDeviceId()))
                    .collect(Collectors.toList());
	    }
		
	

}
