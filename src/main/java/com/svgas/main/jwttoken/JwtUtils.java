package com.svgas.main.jwttoken;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.svgas.main.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils 
{
	/*
	 * This class is has 3 functions:
	 * 	generate jwt from username,date,expiration,secret
	 * 	get username from jwt
	 * 	validate a jwt
	 * 
	 */
	
	@Value("${svgas_new.app.jwtsecret}")
	private String jwtSecret;
	
	@Value("${svgas_new.app.jwtExpiration}")
	private int jwtExpirationMs;
	
	public String generateJwtToken(Authentication authentication)
	{
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512,jwtSecret)
				.compact();
	}
	
	public String getUsernameFromJwtToken(String token)
	{
		return Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateJwtToken(String authToken)
	{
		try
		{
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
}