package com.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.user.dto.UserRegisteredDTO;
import com.user.entity.User;

public interface DefaultUserService extends UserDetailsService {

	User save(UserRegisteredDTO userRegisteredDTO);

}
