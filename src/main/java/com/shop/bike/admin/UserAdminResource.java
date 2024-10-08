package com.shop.bike.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.bike.entity.enumeration.AuthorityType;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.security.jwt.JwtAuthenticationFilter;
import com.shop.bike.security.jwt.TokenProvider;
import com.shop.bike.vm.LoginVM;
import com.shop.bike.web.rest.errors.UsernameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
@Controller
@RequestMapping("/api/v1/admin")
@Slf4j
public class UserAdminResource {
	

	private final AuthenticationManager authenticationManager;

	private final TokenProvider jwtTokenUtil;
	
	

	public UserAdminResource(AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<JWTToken> register(@RequestBody @Valid LoginVM login) throws AuthenticationException {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						login.getUsername(),
						login.getPassword()
				)
		);
		boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch(AuthorityType.ROLE_ADMIN.toString()::equals);
		if(!isAdmin) throw new UsernameNotFoundException(ErrorEnum.UNAUTHORIZED, null);
		
		final String token = jwtTokenUtil.generateToken(authentication);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + token);
		return new ResponseEntity<>(new JWTToken(token), httpHeaders, HttpStatus.OK);
	}
	
	
	/**
	 * Object to return as body in JWT Authentication.
	 */
	static class JWTToken {
		
		private String idToken;
		
		JWTToken(String idToken) {
			this.idToken = idToken;
		}
		
		@JsonProperty("token")
		String getIdToken() {
			return idToken;
		}
		
		void setIdToken(String idToken) {
			this.idToken = idToken;
		}
	}
}
