package com.app.service.imp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.app.dto.ProfileResponseDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.RegisterDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.model.AdvertisementDO;
import com.app.model.UserDO;
import com.app.repository.UserRepository;
import com.app.service.CloudinaryService;
import com.app.service.UserService;
import com.app.utilities.Utility;

import ch.qos.logback.core.status.Status;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;
    
    @Autowired CloudinaryService cloudinaryService;
    
    @Autowired
    private JwtUtil jwtService;

    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
	@Value("${cloudinary.url}")
	private String filePath;
    


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
	public ResponseDTO<UserDTO> getAllAdmins(Integer pageNo, Integer pageSize, String sortBy,String search) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());   
		Page<UserDO> userList = userRepository.findByRoleAndStatus(ROLE.ADMIN,STATUS.ACTIVE,paging,search);
	    List<UserDTO> userDTOlist = Utility.mapList(userList.getContent(), UserDTO.class);
	    return new ResponseDTO<UserDTO>(userList.getTotalElements(),userList.getTotalPages(),userDTOlist);
	}


	@Override
	public ResponseDTO<UserDTO> getAllAttenders(Integer pageNo, Integer pageSize, String sortBy, String search) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());   
		Page<UserDO> userList = userRepository.findByRoleAndStatus(ROLE.ATTENDER,STATUS.ACTIVE,paging,search);
	    List<UserDTO> userDTOlist = Utility.mapList(userList.getContent(), UserDTO.class);
	    return new ResponseDTO<UserDTO>(userList.getTotalElements(),userList.getTotalPages(),userDTOlist);
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
		return new ResultDTO(user.getId().toString(),"Password change successfully");
		
	}


	@Override
	public UserDTO getUserById(Long id) {
		UserDO user = userRepository.findById(id).orElse(null);
		if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
		UserDTO userDTO = Utility.mapObject(user, UserDTO.class);
		if(userDTO.getImage() != null) {
			userDTO.setImage(this.filePath+userDTO.getImage());
		}
		return userDTO;
	}


	@Override
	public UserDTO getByRoleAndStatus(ROLE role, STATUS status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkSuperAdmin() {

		UserDO user = userRepository.findByRole(ROLE.SUPER_ADMIN).orElse(null);
		if(user == null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			userRepository.save(new UserDO("Super","Admin","superadmin@gmail.com","9999999999",encoder.encode("Superadmin@1234"),ROLE.SUPER_ADMIN));
		}
		
	}


	@Override
	public ResultDTO updateUser(Long id,UserDTO userDTO) {
		UserDO user = userRepository.findById(id).orElse(null);
		if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
		
		UserDO newUser = Utility.mapObject(userDTO, UserDO.class);
		
		UserDO updatedUser = Utility.updateObjectWithNonNullFields(user, newUser);
		
		userRepository.save(updatedUser);
		return new ResultDTO(user.getId().toString(),"Updated successfully");
		
	}


	@Override
	public ProfileResponseDTO uploadProfile(ProfileUploadDTO profileUploadDTO) {
		UserDO user = userRepository.findById(profileUploadDTO.getId()).orElse(null);
		if(user == null) {
			 throw new InvalidInputException("No User Present");
		}
		
		var fileName = "profile/" + System.currentTimeMillis() +"_"+ profileUploadDTO.getImage().getOriginalFilename();
	
		System.out.println("user.getImage()-----"+user.getImage()+profileUploadDTO.getImage().getOriginalFilename());
		System.out.println("fileName-----"+fileName);
		if(user.getImage() != null) {
			cloudinaryService.delete(user.getImage() );
		}
//		executorService.execute(() -> {
			 cloudinaryService.upload(profileUploadDTO.getImage(), fileName);

//		});
		user.setImage(fileName);
		userRepository.save(user);

		return new ProfileResponseDTO(this.filePath+fileName);
	}


	@Override
	public String healthCheck() {
		return "SERVER IS RUNINING";
	}



}
