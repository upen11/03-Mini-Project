package com.company.service;

import java.util.List;

import com.company.bindings.ActivateAccount;
import com.company.bindings.Login;
import com.company.bindings.User;

public interface UserMgmtService {

	public boolean saveUser(User user);

	public boolean activateAccount(ActivateAccount activateAcc);

	// operations in view account ??
	public List<User> showAllUsers();

	public User getUserById(Integer userId);

	public boolean deleteUserById(Integer userId);

	// softDelete
	public boolean changeAccStatus(Integer userId, String accStatus);

	// String is taken as return type becoz we want to convey proper msg to the user
	public String login(Login login);

	public String forgotPassword(String email);

}
