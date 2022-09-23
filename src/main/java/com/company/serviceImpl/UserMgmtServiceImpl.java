package com.company.serviceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.company.bindings.ActivateAccount;
import com.company.bindings.Login;
import com.company.bindings.User;
import com.company.entity.UserDetails;
import com.company.repo.UserDetailsRepo;
import com.company.service.UserMgmtService;
import com.company.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserDetailsRepo userDetailsRepo;
	
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {

		UserDetails entity = new UserDetails();
		BeanUtils.copyProperties(user, entity);

		entity.setPassword(generateRandomPwd());
		entity.setAccStatus("In-Active");

		UserDetails save = userDetailsRepo.save(entity);

		String subject = "Registration Almost completed";
		String filename = "REG-MAIL-BODY.txt";
//		a template of mail is in a txt file
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), filename);
//		send registration email..body is dynamic
		boolean sendMail = emailUtils.sendMail(entity.getEmailId(), subject, body);

		System.out.println("Email sent: " + sendMail);

		return save.getUserId() != null;
	}

	@Override
	public boolean activateAccount(ActivateAccount activateAcc) {

		UserDetails entity = new UserDetails();
		entity.setEmailId(activateAcc.getEmailId());
		entity.setPassword(activateAcc.getTempPass());

		Example<UserDetails> example = Example.of(entity);

		List<UserDetails> findAll = userDetailsRepo.findAll(example);

		if (findAll != null && !findAll.isEmpty()) { // check which one is better
			UserDetails userDetail = findAll.get(0);

			userDetail.setPassword(activateAcc.getNewPassword());
			userDetail.setAccStatus("Active");
			userDetailsRepo.save(userDetail);

			return true;
		}

		return false;
	}

	@Override
	public List<User> showAllUsers() {

		List<UserDetails> allUsers = userDetailsRepo.findAll();

		List<User> listUser = new ArrayList<>();

		for (UserDetails details : allUsers) {
			User user = new User();
			BeanUtils.copyProperties(details, user); // whichever variable matches in both class will be copied

			listUser.add(user);
		}

		return listUser;
	}

	@Override
	public User getUserById(Integer userId) {

		Optional<UserDetails> findById = userDetailsRepo.findById(userId);

		if (findById.isPresent()) {
			UserDetails userDetails = findById.get(); // .get is a method of optional
			User user = new User();
			BeanUtils.copyProperties(userDetails, user);

			return user;
		}

		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			userDetailsRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// single method to activate or deactivate the account
	@Override
	public boolean changeAccStatus(Integer userId, String accStatus) {
		Optional<UserDetails> findById = userDetailsRepo.findById(userId);

		if (findById.isPresent()) {
			UserDetails userDetails = findById.get();
			userDetails.setAccStatus(accStatus); // status will be whatever is given by user on pressing the button
			userDetailsRepo.save(userDetails);

			return true;
		}

		return false;
	}

	@Override
	public String login(Login login) {

		UserDetails entity = userDetailsRepo.findByEmailIdAndPassword(login.getEmailId(), login.getPasword());

		/*
		 * UserDetails entity = new UserDetails();
		 * 
		 * entity.setEmailId(login.getEmailId());
		 * entity.setPassword(login.getPasword());
		 * 
		 * // select * from USER_MGMT_APP where emailId = ? and password = ?
		 * Example<UserDetails> example = Example.of(entity); List<UserDetails> findAll
		 * = userDetailsRepo.findAll(example);
		 * 
		 * if(findAll != null && !findAll.isEmpty()) {
		 */
		if (entity != null) {
//			UserDetails userDetails = entity.get(0);
//			if(userDetails.getAccStatus().equals("Active"))
			if (entity.getAccStatus().equals("Active"))
				return "Login Successful";
			else
				return "Account is In-Active. Contact Amdin";
		}

		return "Login Failed. Invalid Credentials";
	}

	@Override
	public String forgotPassword(String email) {
		UserDetails entity = userDetailsRepo.findByEmailId(email); // user defined method

		if (entity == null) {
			return "Invalid Email";
		}

		// TODO: send pwd to user's email
		String subject = "Password Recovery";
		String filename = "RECOVER-PWD-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), filename);
		
		boolean sendMail = emailUtils.sendMail(entity.getFullName(), subject, body);
		
		if(sendMail)
			return "Mail sent successfully";

		return "Error sending mail";
	}

	private String generateRandomPwd() {

		String small = "abcdefghijklmnopqrstuvwxyz";
		String cap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String num = "0123456789";
		String special = "!@#$%^&*()_+";

		String str = small + cap + num + special;

		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int len = 8; // length of temp password

		for (int i = 0; i < len; i++) {
			int idx = random.nextInt(str.length());

			char ch = str.charAt(idx);

			sb.append(ch);
		}

		return sb.toString();
	}

	private String readEmailBody(String fullName, String password, String filename) {

		String mailBody = "";
		String url = "";

		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			StringBuffer sb = new StringBuffer();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				br.readLine();
			}
			
			br.close();
			mailBody = br.toString();

			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{PWD}", password);
			mailBody = mailBody.replace("{URL}", url);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mailBody;
	}

}
