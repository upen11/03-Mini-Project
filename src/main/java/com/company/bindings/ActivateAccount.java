package com.company.bindings;

import lombok.Data;

@Data
public class ActivateAccount {

	private String emailId;
	
	private String tempPass;
	
	private String newPassword;
	
	private String confirmPassword;
}
