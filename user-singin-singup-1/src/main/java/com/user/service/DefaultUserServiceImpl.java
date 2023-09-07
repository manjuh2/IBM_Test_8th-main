package com.user.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.dto.UserRegisteredDTO;
import com.user.entity.Role;
import com.user.entity.User;
import com.user.repository.RoleRepository;
import com.user.repository.UserRepository;

@Service
public class DefaultUserServiceImpl implements DefaultUserService{
   @Autowired
	private UserRepository userRepo;
   @Autowired
    private RoleRepository roleRepo;
   
   
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}
	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {
	    Role role = null;
	    if (userRegisteredDTO.getRole().equals("USER")) {
	        role = roleRepo.findByRole("USER");
	    } else if (userRegisteredDTO.getRole().equals("ADMIN")) {
	        role = roleRepo.findByRole("ADMIN");
	    }

	    User user = new User();
	    user.setEmail(userRegisteredDTO.getEmail_id());
	    user.setName(userRegisteredDTO.getName());
	    user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
	    user.getRoles().add(role); // Directly add the role to the user's roles set
        
	    return userRepo.save(user);
	}
//	@Override
//	public User save(UserRegisteredDTO userRegisteredDTO) {
//		Role role = new Role();
//		if(userRegisteredDTO.getRole().equals("USER"))
//		  role = roleRepo.findByRole("USER");
//		else if(userRegisteredDTO.getRole().equals("ADMIN"))
//		 role = roleRepo.findByRole("ADMIN");
//		User user = new User();
//		user.setEmail(userRegisteredDTO.getEmail_id());
//		user.setName(userRegisteredDTO.getName());
//		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
//		user.setRoles(role);
//		
//		return userRepo.save(user);
//	}
}
