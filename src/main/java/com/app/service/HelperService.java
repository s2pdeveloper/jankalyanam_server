package com.app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.dto.DistrictDTO;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.StateDTO;
import com.app.dto.TahsilDTO;
import com.app.dto.TahsilRequestDTO;
import com.app.dto.VillageDTO;
import com.app.dto.VillageRequestDTO;
import com.app.model.DistrictDO;
import com.app.model.DonorDO;
import com.app.model.StateDO;
import com.app.model.TahsilDO;
import com.app.model.VillageDO;


@Service
public interface HelperService {

	ResultDTO assignOrRemoveToBloodRequest(Long id, Boolean allocate);


	void acceptOrCancelByDonor(Long id, Boolean add);


	void CancelBloodRequest(Long id);


	ResultDTO bulkInsertState(List<StateDO> states);


	ResultDTO bulkInsertDistrict(List<DistrictDO> district);


	ResultDTO bulkInsertTahsil(List<TahsilDO> tahsil);


	ResultDTO bulkInsertVillage(List<VillageDO> village);


	List<StateDO> getAll();


	List<StateDTO> getAllState();


	List<DistrictDTO> getDistrictByStateId(Long stateId);


	List<TahsilDTO> getTahsilByDistrictId(Long districtId);


	List<VillageDTO> getVillageByTahsilId(Long tahsilId);


	ResultDTO addTahsil(TahsilRequestDTO tahsil);


	ResultDTO updateTahsil(Long id, TahsilRequestDTO tahsil);


	ResultDTO deleteTahsil(Long id);


	TahsilDTO getTahsilById(Long id);


	ResultDTO addVillage(VillageRequestDTO village);


	ResultDTO updateVillage(Long id, VillageRequestDTO village);


	ResultDTO deleteVillage(Long id);


	VillageDTO getVillageById(Long id);

   
}