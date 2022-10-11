package com.company.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.bindings.ActivateAccount;
import com.company.bindings.Login;
import com.company.bindings.User;
import com.company.service.UserMgmtService;

@RestController
public class UserRestController {

	@Autowired
	private UserMgmtService service;

	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user) {
		boolean isSaved = service.saveUser(user);

		if (isSaved) {
			return new ResponseEntity<>("Registration successful", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate") // form submission
	public ResponseEntity<String> activateAcc(@RequestBody ActivateAccount acc) {
		boolean isActivated = service.activateAccount(acc);

		if (isActivated) {
			return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Account not activated", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = service.showAllUsers();

		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
		User user = service.getUserById(userId);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean isDeleted = service.deleteUserById(userId);

		if (isDeleted) {
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/status/{userId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId, @PathVariable String status) {
		boolean isChanged = service.changeAccStatus(userId, status);

		if (isChanged) {
			return new ResponseEntity<>("Status changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Status changed failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		String status = service.login(login);

		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@GetMapping("/forgotpwd/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email) {
		String status = service.forgotPassword(email);

		return new ResponseEntity<>(status, HttpStatus.OK);
	}

}
