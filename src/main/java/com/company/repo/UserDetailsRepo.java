package com.company.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.entity.UserDetails;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails, Integer> {

	public UserDetails findByEmailIdAndPassword(String emailId, String pasword);

	public UserDetails findByEmailId(String email); // findBy..Emaild(..) "emailId" is my variable in entity class thats
													// why I have to write method as "findByEmailId" not findByEmail.

}
