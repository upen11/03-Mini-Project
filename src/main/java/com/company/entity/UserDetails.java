package com.company.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "USER_DETAILS")
@Data
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	private String fullName;

	private String emailId;

	private String password;

	private Long mobNo;

	private Character gender;

	private String dob;

	private Long ssn;

	private String accStatus;

	private String createdBy;

	private String updatedBy;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDate creationDate;

	@Column(insertable = false)
	@UpdateTimestamp
	private LocalDate updationDate;

}
