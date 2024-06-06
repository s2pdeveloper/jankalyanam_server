package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.ChangePasswordDTO;
import com.app.dto.ForgetResponseDTO;
import com.app.dto.LoginDTO;
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.RegisterDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.dto.UserUpdateDTO;
import com.app.model.UserDO;
import com.app.repository.UserRepository;

@Service
public interface UserService {
	
	 public ResultDTO register(RegisterDTO user);
	 
	 public UserDTO login(LoginDTO user) throws Exception;
   
	 public UserDO saveUser(UserDO user);
   
	 public UserDO getUser(Long id);
   
	 public List<UserDTO> getAllUsers();
   
	 public ResultDTO deleteUser(Long id);

	 public ResponseDTO<UserDTO> getAllAdmins(Integer pageNo, Integer pageSize, String sortBy, String search);

	 public ResponseDTO<UserDTO> getAllAttenders(Integer pageNo, Integer pageSize, String sortBy, String search);

	 public ResultDTO changePassword(ChangePasswordDTO changePasswordDTO);

	 public UserDTO getUserById(Long id);
	 
	 public void checkSuperAdmin();
	 
	 public UserDTO getByRoleAndStatus(ROLE role , STATUS status) ;

	public UserDTO updateUser(Long id, UserUpdateDTO userDTO);

	public ProfileResponseDTO uploadProfile(ProfileUploadDTO profileUploadDTO);

	public String healthCheck();

	public ForgetResponseDTO forget(String mobileNo);

	public ResultDTO sendMail(String mobileNo, String email);

	public ResultDTO verify(String mobileNo, String otp);

	public ResultDTO setPassword(String mobileNo, String password);

	
}