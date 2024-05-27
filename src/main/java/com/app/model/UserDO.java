
package com.app.model;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@NoArgsConstructor
@AllArgsConstructor 
@ToString
@Data
@Entity
@Table (name = "User")
public class UserDO implements UserDetails {
    private static final long serialVersionUID = 1L;

    public UserDO(String firstName,String lastName,String email,String  mobileNo,String password,ROLE role){
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.email = email;
    	this.mobileNo = mobileNo;
    	this.password = password;
    	this.role = role;
    }
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private String  email;
    
    @Column(name = "mobile_no",unique=true)
    private String  mobileNo;
    
    private String  state;
    
    private String  district;
    
    private String  tahsil;
    
    private String  village;
    
    private Long  pincode;
    
    private String  password;
    
    private String  image;
    
    private int  otp;
    
    private LocalDate  DOB;
    
    @Column(name = "blood_group")
    private String bloodGroup;
    
    @Enumerated(EnumType.STRING)
    private STATUS status = STATUS.ACTIVE; 
    
    @Enumerated(EnumType.STRING)
    private ROLE role;
    
    @Column(name = "created_at",nullable = false, updatable=false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at",nullable = false, updatable=true)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return  List.of(new SimpleGrantedAuthority(role.name()));
	}
	@Override
	public String getUsername() {
		return mobileNo;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
    
	
	@OneToMany(mappedBy = "acceptor",fetch = FetchType.LAZY)
    private List<BloodRequestDO> myList ;

	@OneToMany(mappedBy = "attender",fetch = FetchType.LAZY)
    private List<BloodRequestDO> bloodRequestList ;
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<DonorDO> bloodDonateList ;
	
    @OneToOne(mappedBy = "userDetail", fetch = FetchType.LAZY)
    private UserDeviceIdDO device;
    
//    @JsonIgnoreProperties("user")
  
//    @JsonBackReference
	
//	@JsonIgnore
//	@OneToMany(mappedBy = "user")
//	@JsonManagedReference
//    private List<BloodRequest> bloodRequestList ;
	
//	 @OneToMany(mappedBy = "user")
//	 private List<BloodRequest> bloodRequestList = new ArrayList<>();
    


 
}