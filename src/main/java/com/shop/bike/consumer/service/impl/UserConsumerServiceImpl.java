package com.shop.bike.consumer.service.impl;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.dto.ProfileConsumerDTO;
import com.shop.bike.consumer.dto.RegisterConsumerDTO;
import com.shop.bike.consumer.dto.mapper.ProfileConsumerMapper;
import com.shop.bike.consumer.repository.UserConsumerRepository;
import com.shop.bike.consumer.service.UserConsumerService;
import com.shop.bike.consumer.vm.ProfileConsumerVM;
import com.shop.bike.consumer.vm.mapper.ProfileConsumerVMMapper;
import com.shop.bike.entity.Otp;
import com.shop.bike.entity.Role;
import com.shop.bike.entity.User;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.repository.OtpRepository;
import com.shop.bike.repository.RoleRepository;
import com.shop.bike.security.AuthoritiesConstants;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.MailService;
import com.shop.bike.service.dto.MailRequestDTO;
import com.shop.bike.service.dto.OtpDTO;
import com.shop.bike.service.dto.mapper.OtpMapper;
import com.shop.bike.service.impl.UserServiceImpl;
import com.shop.bike.utils.Utils;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.shop.bike.service.impl.OtpServiceImpl.generateRandomAlphanumericString;

@Service
@Transactional
@Qualifier(ApplicationConstant.CONSUMER)
@Slf4j
public class UserConsumerServiceImpl extends UserServiceImpl implements UserConsumerService {

	@Autowired
	private ProfileConsumerVMMapper profileConsumerVMMapper;

	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private UserConsumerRepository userConsumerRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ProfileConsumerMapper profileConsumerMapper;

	@Autowired
	private MailService mailService;

	@Autowired
	private OtpMapper otpMapper;


	/*************************************************************
	 *
	 * @decription: get current detail user
	 *
	 * @param: token when authenticate
	 * @return: profile vm
	 *
	 * @date: 04/05/2023
	 * @author: tuanhtran1 (tu.tran@ecaraid.com)
	 **************************************************************/
	@Override
	public ProfileConsumerVM getCurrentProfileConsumer() {
		Long userId = Long.valueOf(SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND)));

		ProfileConsumerVM profileConsumerVM = profileConsumerVMMapper.toDto(this.findConsumerById(userId)
				.orElseThrow(() -> new BadRequestAlertException(ErrorEnum.USER_BLOCKED)));
		return profileConsumerVM;
	}

	@Override
	public void checkExist(String email) {
		Optional<User> optionalUser = userConsumerRepository.findOneWithAuthoritiesByLogin(email);
		if (optionalUser.isPresent()) {
			throw new BadRequestAlertException(ErrorEnum.USERNAME_IS_EXISTED);
		}
	}

	@Override
	public ProfileConsumerVM createProfileConsumer(RegisterConsumerDTO dto) {
		Optional<Otp> optionalOtp = otpRepository.findByUserNameAndActiveKey(dto.getUsername(), dto.getActivatedKey());
		if(optionalOtp.isPresent()){
			User consumer = new User();
			consumer.setName(dto.getName());
			consumer.setUserName(optionalOtp.get().getUserName());
			consumer.setPhone(dto.getPhone());
			consumer.setEmail(consumer.getUserName());
			consumer.setStatus(1); //activated user
			if(dto.getAvatar()==null) {
				consumer.setAvatar("Avatar_icon_green.png");
			}
			String encryptedPassword = passwordEncoder.encode(dto.getPassword());
			consumer.setPassword(encryptedPassword);
			List<Role> roles = new ArrayList<>();
			roleRepository.findByCode(AuthoritiesConstants.CONSUMER).map(roles::add);
			consumer.setRoles(roles);
			return profileConsumerVMMapper.toDto(userConsumerRepository.save(consumer));
		}
		else throw new BadRequestAlertException(ErrorEnum.USER_NOT_ACTIVATED_BY_KEY);
	}

	@Override
	public ProfileConsumerVM updateProfile(ProfileConsumerDTO dto) {
		Long userId = Long.valueOf(SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND)));
		User consumer = this.findConsumerById(userId).get();
		profileConsumerMapper.partialUpdate(consumer, dto);
		userConsumerRepository.save(consumer);

		return profileConsumerVMMapper.toDto(consumer);
	}

	@Override
	public void updateAvatar(String avatar) {
		String userId = SecurityUtils.getCurrentUserLogin().orElseThrow(()-> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND));
		User user = userConsumerRepository.findById(Long.parseLong(userId))
				.orElseThrow(()-> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND));
		user.setAvatar(avatar);
		userConsumerRepository.save(user);
	}

	


}