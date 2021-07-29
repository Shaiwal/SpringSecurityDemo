package com.demo.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.security.entitiy.UserEntity;
import com.demo.security.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		List<UserEntity> users = userRepo.findByUserName(username);
		if(users == null || users.size()==0) {
			throw new UsernameNotFoundException("User with following username oesn't exisit: "+username);
		}
		List<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		ga.add(new SimpleGrantedAuthority(users.get(0).getRoles()));
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		User user = new User(users.get(0).getUserName(), encoder.encode(users.get(0).getPassword()), ga);
		return user;
	}

}
