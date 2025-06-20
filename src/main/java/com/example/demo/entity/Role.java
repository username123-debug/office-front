package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Role {
	private Long id;
	private String role;
	
	@OneToMany
	List<User> user ;
}
