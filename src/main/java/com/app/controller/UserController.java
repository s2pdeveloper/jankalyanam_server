package com.app.controller;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.ChangePasswordDTO;
import com.app.dto.LoginDTO;
import com.app.dto.ProfileUploadDTO;
import com.app.dto.RegisterDTO;
import com.app.dto.ResponseDTO;
import com.app.dto.ResultDTO;
import com.app.dto.UserDTO;
import com.app.model.UserDO;
import com.app.service.UserService;
import io.swagger.annotations.Api;


@RestController
@RequestMapping("/user")
@Api(tags = {"user"})
public class UserController {
	
	@Autowired
	 private UserService userService;
	 

	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@GetMapping("/admins")
	public ResponseDTO<UserDTO> getAllAdmins(
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String search) {
		return userService.getAllAdmins(pageNo,pageSize,sortBy,search);	
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@GetMapping("/attenders")
	public ResponseDTO<UserDTO> getAllAttenders(
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String search) {
		return userService.getAllAttenders(pageNo,pageSize,sortBy,search);
	}
	
	@PostMapping("/register")
	public ResultDTO register(@RequestBody @Valid RegisterDTO user){
		return userService.register(user);
		
	}
	
	@PostMapping("/login")
	public UserDTO login(@RequestBody  @Valid LoginDTO user) throws Exception{
		return userService.login(user);
		}
	
	@PostMapping("/change")
	public ResultDTO change(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
		return userService.changePassword(changePasswordDTO);	
	}
	
	@PutMapping("/profile")
	public String update(ProfileUploadDTO profileUploadDTO) {
		return userService.uploadProfile(profileUploadDTO);	
	}
	
	@PutMapping("/{id}")
	public ResultDTO update(@PathVariable Long id,@RequestBody UserDTO userDTO) {
		return userService.updateUser(id,userDTO);	
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	@DeleteMapping("/{id}")
	public ResultDTO delete(@PathVariable Long id) {
		return userService.deleteUser(id);	
	}
	
	
	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable Long id) {
		return userService.getUserById(id);	
	}
}
