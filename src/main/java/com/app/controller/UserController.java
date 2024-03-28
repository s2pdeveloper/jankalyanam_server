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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.constant.ServiceConstant.ROLE;
import com.app.dto.ChangePasswordDTO;
import com.app.dto.LoginDTO;
import com.app.dto.RegisterDTO;
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
	 

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admins")
	public List<UserDTO> getAllAdmins() {
		return userService.getAllAdmins();	
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/attenders")
	public List<UserDTO> getAllAttenders() {
		return userService.getAllAttenders();
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
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{id}")
	public ResultDTO delete(@PathVariable Long id) {
		return userService.deleteUser(id);	
	}
	
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
//	@RolesAllowed("ROLE_ADMIN")
	public UserDTO getUserById(@PathVariable Long id) {
		return userService.getUserById(id);	
	}
}
