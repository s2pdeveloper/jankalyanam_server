package com.app.constant;

public class ServiceConstant {

	public enum ROLE {
	   ADMIN,SUPER_ADMIN,ATTENDER
	}
	
	public enum STATUS {
		   ACTIVE,INACTIVE,DELETED
		}
	
	public enum BLOOD_STATUS {
		   PENDING,ACCEPTED,ALLOCATED,RECEIVED,DONE
		}
	
	public enum DONOR_STATUS {
		PENDING,ALLOCATED,ACCEPTED,DONE,CLOSE
		}
	
	public enum PROVIDED {
		 DONOR,BLOOD_BANK
		}
	
	public enum GENDER {
		 MALE,FEMALE,OTHER
		}
	
//	public enum BloodGroup {
//		   A_POSITIVE,  
//		    A_NEGATIVE ,
//		    B_POSITIVE,
//		    B_NEGATIVE,
//		    AB_POSITIVE,
//		    AB_NEGATIVE,
//		    O_POSITIVE,
//		    O_NEGATIVE 
//
//		   
//	}
}
