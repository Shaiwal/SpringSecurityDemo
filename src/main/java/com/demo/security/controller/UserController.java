package com.demo.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.security.dto.AuthenticationRequest;
import com.demo.security.utils.*;

@RestController
public class UserController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping(path = "/authenticate", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> authneticate(@RequestBody AuthenticationRequest authRequest){//@RequestParam(name = "username") String username, @RequestParam(name = "password") String password){
		try {
			//flow control will go to authentication provider from here
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));//username, password));//
		}catch (BadCredentialsException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new BadCredentialsException(authRequest.getUsername());//username);//
		}
		String jwtToken = jwtUtils.generateToken(authRequest.getUsername());//username);//
		return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
	}
	
	@GetMapping(path = "/admin")
	public String viewAdmin() {
		return "Hello Admin";
	}
	@GetMapping(path = "/all")
	public String viewAll() {
		return "Hello All";
	}
	@GetMapping(path = "/user")
	public String viewUser() {
		return "Hello User";
	}

}
