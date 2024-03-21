package com.app.model;



import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.constant.ServiceConstant.ROLE;
import com.app.constant.ServiceConstant.STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@NoArgsConstructor
@AllArgsConstructor 
@Getter
@Setter
@Entity
@Table (name = "User")
public class UserDO implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private String  email;
    
    @Column(name = "mobile_no")
    private String  mobileNo;
    
    private String  state;
    
    private String  city;
    
    private String  address;
    
    private Long  pincode;
    
    private String  password;
    
    private STATUS status = STATUS.ACTIVE; 
    
    private ROLE role;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
    
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
    
	
	@OneToMany(mappedBy = "acceptor")
    private List<BloodRequestDO> myList ;

	@OneToMany(mappedBy = "attender")
    private List<BloodRequestDO> bloodRequestList ;
	
	@OneToMany(mappedBy = "user")
    private List<DonorDO> bloodDonateList ;
  
//    @JsonIgnoreProperties("user")
  
//    @JsonBackReference
	
//	@JsonIgnore
//	@OneToMany(mappedBy = "user")
//	@JsonManagedReference
//    private List<BloodRequest> bloodRequestList ;
	
//	 @OneToMany(mappedBy = "user")
//	 private List<BloodRequest> bloodRequestList = new ArrayList<>();
    


 
}