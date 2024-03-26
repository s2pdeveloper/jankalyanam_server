package com.app.service.imp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.config.JwtUtil;
import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.app.dto.ChangePasswordDTO;
import com.app.dto.LoginDTO;
import com.app.dto.RegisterDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.model.UserDO;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import com.app.utilities.Utility;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtService;

    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    


    public UserDO saveUser(UserDO user) {
        return userRepository.save(user);
    }
    
    
    public UserDO getUser(Long id) {
    	UserDO user = userRepository.findById(id).orElse(null);
    	if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
    	return user;
    }
    
    
    public List<UserDTO> getAllUsers(){
    	List<UserDO> userList = userRepository.findAll();
        List<UserDTO> userDTOlist = Utility.mapList(userList, UserDTO.class);
        return userDTOlist;

    }
    
    public ResultDTO deleteUser(Long id) {
    	UserDO user = userRepository.findById(id).orElse(null);
    	if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
    	user.setStatus(STATUS.DELETED);
    	userRepository.save(user);
    	return new ResultDTO(id.toString(),"User Deleted Successfully");
    	
    }
    
	@Override
	public ResultDTO register(RegisterDTO user) {
		if(userRepository.existsByMobileNo(user.getMobileNo())) {
			 throw new InvalidInputException("Mobile No already exits");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		UserDO userDO = Utility.mapObject(user, UserDO.class);
		UserDO saveData = userRepository.save(userDO);
		return new ResultDTO(saveData.getId().toString(),"Register Sucessfully");

	}
	
	@Override
	public UserDTO login(LoginDTO user) throws Exception {
	      try {
	          authenticationManager.authenticate(
	             new
	             UsernamePasswordAuthenticationToken(user.getMobileNo(),
	             user.getPassword())
	          );
	       } catch (DisabledException e) {
	          throw new InvalidInputException("USER Is DISABLED");
	       } catch (BadCredentialsException e) {
	          throw new InvalidInputException("INVALID CREDENTIALS");
	       }
	       final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getMobileNo());
	       final String jwtToken = jwtService.generateToken(userDetails);
	       UserDTO userDTO = Utility.mapObject(userDetails, UserDTO.class);
	       userDTO.setToken(jwtToken);
	       return userDTO;
	}


	@Override
	public List<UserDTO> getAllAdmins() {
	       List<UserDO> userList = userRepository.findByRoleAndStatus(ROLE.ADMIN,STATUS.ACTIVE);
	       List<UserDTO> userDTOlist = Utility.mapList(userList, UserDTO.class);
	        return userDTOlist;
	}


	@Override
	public List<UserDTO> getAllAttenders() {
	      List<UserDO> userList = userRepository.findByRoleAndStatus(ROLE.ATTENDER,STATUS.ACTIVE);
	      List<UserDTO> userDTOlist = Utility.mapList(userList, UserDTO.class);
	      return userDTOlist;
	}


	@Override
	public ResultDTO changePassword(ChangePasswordDTO changePasswordDTO) {
		UserDO user = userRepository.findById(Utility.getSessionUser().getId()).orElse(null);
		if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
	
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(changePasswordDTO.getNewPassword()));
		userRepository.save(user);
		new ResultDTO(user.getId().toString(),"Password change successfully");
		
	
		return null;
	}


	@Override
	public UserDTO getUserById(Long id) {
	    System.out.println("userDetails-------"+Utility.getSessionUser().toString());
		UserDO user = userRepository.findById(id).orElse(null);
		if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
		return Utility.mapObject(user, UserDTO.class);
	}


	@Override
	public UserDTO getByRoleAndStatus(ROLE role, STATUS status) {
		// TODO Auto-generated method stub
		return null;
	}

	



}
