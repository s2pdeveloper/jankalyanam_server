package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.app.dto.ChangePasswordDTO;
import com.app.dto.LoginDTO;
import com.app.dto.RegisterDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
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

	 public List<UserDTO> getAllAdmins();

	 public List<UserDTO> getAllAttenders();

	 public ResultDTO changePassword(ChangePasswordDTO changePasswordDTO);

	 public UserDTO getUserById(Long id);
}