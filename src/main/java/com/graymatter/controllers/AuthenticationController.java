package com.graymatter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graymatter.configuration.UserPrincipal;
import com.graymatter.dtos.LoginResponse;
import com.graymatter.dtos.LoginUserDto;
import com.graymatter.dtos.RegUserDto;
import com.graymatter.entities.User;
import com.graymatter.service.AuthenticationService;
import com.graymatter.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	JwtService jwtService;
	@Autowired
	AuthenticationService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<User> signUp(@RequestBody RegUserDto regUserDto){
		User regUser=authService.signUp(regUserDto);
		return ResponseEntity.ok(regUser);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto){
		
		User authUser=authService.login(loginUserDto);
		String token=jwtService.generateToken(new UserPrincipal(authUser));
		LoginResponse loginResponse=new LoginResponse();
		loginResponse.setToken(token);
		loginResponse.setExpirationTime(jwtService.expirationTime());
		return ResponseEntity.ok(loginResponse);
	}
	
}
















