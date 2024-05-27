package com.app.service.imp;

import java.lang.reflect.Field;
import java.util.* ;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.app.constant.ServiceConstant;
import com.app.constant.ServiceConstant.BLOOD_STATUS;
import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.constant.ServiceConstant.PROVIDED;
import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.BloodDTO;
import com.app.dto.BloodRequestDTO;
import com.app.dto.BloodRequestUpdateDTO;
import com.app.dto.DonorDTO;
import com.app.dto.MonthlyCountDTO;
import com.app.dto.NotificationRequest;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.model.BloodRequestDO;
import com.app.model.DistrictDO;
import com.app.model.DonorDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.UserDO;
import com.app.model.VillageDO;
import com.app.repository.BloodRequestRepository;
import com.app.repository.DistrictRepositoy;
import com.app.repository.DonorRepository;
import com.app.repository.StateRepositoy;
import com.app.repository.TahsilRepositoy;
import com.app.repository.VillageRepositoy;
import com.app.service.BloodRequestService;
import com.app.service.DonorService;
import com.app.service.HelperService;
import com.app.service.UserDeviceIdService;
import com.app.utilities.Utility;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;



@Service
@Slf4j
public class HelperServiceImp implements HelperService{
   
	@Autowired
    private BloodRequestRepository bloodRequestRepository;
	
	@Autowired
    private DonorRepository donorRepository;
	
	@Autowired
    private StateRepositoy stateRepository;
	
	@Autowired
    private DistrictRepositoy districtRepositoy;
	
	@Autowired
    private TahsilRepositoy tahsilRepositoy;
	
	@Autowired
    private VillageRepositoy villageRepositoy;
	
	@Autowired
	@Qualifier("cachedThreadPool")
	private ExecutorService executorService;
	
	@Autowired
	private UserDeviceIdService userDeviceIdService;
	
	@Autowired
	private FCMService fcmService;

	@Override
	public ResultDTO assignOrRemoveToBloodRequest(Long id,Boolean allocate) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
	
		if(allocate) {
			data.setStatus(DONOR_STATUS.ALLOCATED);
		}else {
			data.setStatus(DONOR_STATUS.PENDING);
		}
		
		data.setLocation(null);
		donorRepository.save(data);
		executorService.execute(() -> {
			String title = "Donation";
			String body = null;
			String deviceId = null;
			if(allocate) {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  body =String.format("A Beneficier %s has been Allocated.Location %s",
						  donor.getBloodRequest().getName(),
	                     donor.getBloodRequest().getHospitalName());
				  deviceId = userDeviceIdService.getDeviceId(donor.getUserId());	  
			
			}
			else  {
				  DonorDO donor = donorRepository.findById(id).orElse(null);
				  body =String.format("Thanks for your support %s But the beneficier got another Donor.You can donate blood to the specified blood bank",
	                     donor.getName());
				  deviceId = userDeviceIdService.getDeviceId(donor.getUserId());	  
			
			}

			if(StringUtils.hasText(deviceId)) {
				NotificationRequest notify = new NotificationRequest(title,body,Arrays.asList(deviceId));
				try {
					fcmService.sendMessageToToken(notify);
				} catch (FirebaseMessagingException | InterruptedException | ExecutionException e) {
					log.info("--------ERROR IN FIREBASE---------");
					e.printStackTrace();
					
				}
			}
			
		});
		
		return new ResultDTO(id.toString(),"Updated Successfully!");
	}

	@Override
	public void acceptOrCancelByDonor(Long id,Boolean add) {
		Optional<DonorDO> donorDetails = donorRepository.findById(id);
		DonorDO data = donorDetails.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		if(donorDetails.get().getBloodRequest() != null && add) {
			Optional<BloodRequestDO> bloodRequestData = bloodRequestRepository.findById(donorDetails.get().getBloodRequest().getId());
			BloodRequestDO b = bloodRequestData.orElse(null);
			if(b != null) {
				b.setStatus(BLOOD_STATUS.ALLOCATED);
				bloodRequestRepository.save(b);
			}
		
			
		}
		else if(donorDetails.get().getBloodRequest() != null && !add) {
			Optional<BloodRequestDO> bloodRequestData = bloodRequestRepository.findById(donorDetails.get().getBloodRequest().getId());
			BloodRequestDO b = bloodRequestData.orElse(null);
			if(b != null) {
			b.setStatus(BLOOD_STATUS.ACCEPTED);		
			b.setDonorId(null);		
			bloodRequestRepository.save(b);
			}
		}
		
	}
	
	@Override
	public void CancelBloodRequest(Long id) {
		Optional<BloodRequestDO> bloodRequestData = bloodRequestRepository.findById(id);
		BloodRequestDO data = bloodRequestData.orElse(null);
		if(data == null) {
			throw new InvalidInputException("Invalid Input");
		}
		if(data.getDonorId() != null) {
			Optional<DonorDO> donorDetails = donorRepository.findById(data.getDonorId());
			DonorDO donarDetails = donorDetails.orElse(null);
			if(donarDetails != null) {
				donarDetails.setStatus(DONOR_STATUS.PENDING);
				donorRepository.save(donarDetails);
			}
			data.setDonorId(null);	
			bloodRequestRepository.save(data);
		}
		
		
	}

	@Override
	public ResultDTO bulkInsertState(List<StateDO> states) {
		stateRepository.saveAll(states);
		return new ResultDTO("","States Inserted Successfully!");
	}

	@Override
	public ResultDTO bulkInsertDistrict(List<DistrictDO> district) {
		districtRepositoy.saveAll(district);
		return new ResultDTO("","District Inserted Successfully!");
	}

	@Override
	public ResultDTO bulkInsertTahsil(List<TahsilDO> tahsil) {
		tahsilRepositoy.saveAll(tahsil);
		return new ResultDTO("","Tahsil Inserted Successfully!");
	}

	@Override
	public ResultDTO bulkInsertVillage(List<VillageDO> village) {
		villageRepositoy.saveAll(village);
		return new ResultDTO("","Village Inserted Successfully!");
	}
	
	@Override
	public List<StateDO> getAll() {
		return stateRepository.findAll();
	}
}
