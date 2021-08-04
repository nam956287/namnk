package com.edu.repositories;

import org.springframework.data.repository.CrudRepository;

import com.edu.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	
}
