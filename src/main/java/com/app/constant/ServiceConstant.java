package com.app.constant;

public class ServiceConstant {

	public enum ROLE {
	   ADMIN,SUPER_ADMIN,ATTENDER
	}
	
	public enum STATUS {
		   ACTIVE,INACTIVE,DELETED
		}
	
	public enum BLOOD_STATUS {
		   PENDING,ACCEPTED,RECEIVED,DONE
		}
	
	public enum DONOR_STATUS {
		PENDING,DONE,CLOSE
		}
	
	public enum PROVIDED {
		 DONOR,BLOOD
		}
	
	public enum GENDER {
		 MALE,FEMALE,OTHER
		}
	
	public enum BloodGroup {
	    A_POSITIVE,  // Blood Group A positive
	    A_NEGATIVE,  // Blood Group A negative
	    B_POSITIVE,  // Blood Group B positive
	    B_NEGATIVE,  // Blood Group B negative
	    AB_POSITIVE, // Blood Group AB positive
	    AB_NEGATIVE, // Blood Group AB negative
	    O_POSITIVE,  // Blood Group O positive
	    O_NEGATIVE   // Blood Group O negative
	}
}
