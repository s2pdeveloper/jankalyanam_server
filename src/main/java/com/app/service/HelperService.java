package com.app.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.DONOR_STATUS;
import com.app.dto.DonorDTO;
import com.app.dto.DonorRequestDTO;
import com.app.dto.DonorRequestUpdateDTO;
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
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

   
}