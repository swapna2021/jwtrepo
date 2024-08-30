package com.graymatter.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.graymatter.configuration.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${security.jwt.secret-key}")
	String secretKey;
	@Value("${security.jwt.expiration-time}")
	private long expTime;

	

	public String generateToken(UserPrincipal userPrincipal) {
		
		return generateToken(new HashMap<String , Object>(),userPrincipal);
	}
	
	private String generateToken(HashMap<String, Object> claims, UserPrincipal userPrincipal) {
		
		return buildToken(claims,userPrincipal,expTime);
	}

	private String buildToken(HashMap<String, Object> claims, UserPrincipal userPrincipal, long expTime) {
		
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+expTime))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
	}

	private Key getSignInKey() {
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public long expirationTime() {
		return expTime;
	}
	

	public Claims extractClaims(String token) {
		
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String extractUserName(String token) {	
		return extractClaims(token).getSubject();
	}
	
	public Date extractExpirationTime(String token) {
		return extractClaims(token).getExpiration();
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		
		String email=extractUserName(token);
		
		return (email.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}

	
	
	private boolean isTokenExpired(String token) {
		
		return extractClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
	}	
	

	

	

}
