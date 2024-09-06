package com.shop.bike.consumer.rest;

import com.shop.bike.consumer.dto.RequestBossPaymentDTO;
import com.shop.bike.entity.enumeration.PaymentParam;
import com.shop.bike.service.dto.PaymentParamDTO;
import com.shop.bike.utils.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class MaxxPaymentConsumerResource {
	


    @PostMapping("/public")
	public String home(Model model, @RequestParam PaymentParam paymentParam, @RequestBody PaymentParamDTO paymentParamDTO) {
		model.addAttribute("message", "Welcome to the Spring Boot application!");

		// Định dạng thời gian hiện tại để đặt tên file
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		String timestamp = LocalDateTime.now().format(formatter);
		String logFileName = "log_" + timestamp + ".log";

		// Chuyển đổi đối tượng PaymentParamDTO thành JSON
		String json = JsonConverter.toJson(paymentParamDTO);

		// Ghi thông tin JSON vào file .log
		try {
			Files.write(Paths.get(logFileName), json.getBytes(), StandardOpenOption.CREATE);
			log.info("Logged paymentParamDTO to file: " + logFileName);
		} catch (IOException e) {
			log.error("Failed to write to log file", e);
		}

		// Điều kiện điều hướng
		if (paymentParam.equals(PaymentParam.SC_SUCCESSURL)) {
			return "paymentSuccess";
		} else if (paymentParam.equals(PaymentParam.SC_FAILURL)) {
			return "paymentFail";
		} else {
			return "paymentCancel";
		}
	}
	
	@PostMapping("/public/get-sign")
	private ResponseEntity<Map<String, String>> responseSign(@RequestBody RequestBossPaymentDTO requestBossPaymentDTO) {
		Map<String, String> signMap = new HashMap<>();
		signMap.put("amount", requestBossPaymentDTO.getAmount());
		signMap.put("mchOrderNo", requestBossPaymentDTO.getMchOrderNo());
		signMap.put("subject", requestBossPaymentDTO.getSubject());
		signMap.put("wayCode", requestBossPaymentDTO.getWayCode());
		signMap.put("reqTime", requestBossPaymentDTO.getReqTime());
		signMap.put("body", requestBossPaymentDTO.getBody());
		signMap.put("version", requestBossPaymentDTO.getVersion());
		signMap.put("appId", requestBossPaymentDTO.getAppId());
		signMap.put("notifyUrl", requestBossPaymentDTO.getNotifyUrl());
		signMap.put("signType", requestBossPaymentDTO.getSignType());
		signMap.put("currency", requestBossPaymentDTO.getCurrency());
		signMap.put("mchNo", requestBossPaymentDTO.getMchNo());

		String privateKey = "7pZqH254we5N679C344EKkLNd3FzMIfkSN8Bib4wvuqHF8ism6Xg4Yb39UpsfilKF1jl91gyLgRtCHiz96DIqqJSdTGZNv2F4ixA0V43RtXNJme1qUFLhacjPXvooJAa";
		String sign = generateSign(signMap, privateKey);
		Map<String,String> signStringMap = new HashMap<>();
		signStringMap.put("sign",sign);
		signStringMap.putAll(signMap);
		return ResponseEntity.ok(signStringMap);
	}

	public static String generateSign(Map<String, String> params, String privateKey) {
		// step 1: remove value null or empty
		Map<String, String> filteredParams = new HashMap<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {

			if (entry.getValue() != null && !entry.getValue().isEmpty()) {
				filteredParams.put(entry.getKey(), entry.getValue());
			}
		}

		// Bước 2: order by ASCII (dictionary order)
		List<String> sortedKeys = new ArrayList<>(filteredParams.keySet());
		Collections.sort(sortedKeys);


		// Bước 3: create string like style key=value and connect = &
		StringBuilder stringA = new StringBuilder();
		for (int i = 0; i < sortedKeys.size(); i++) {
			String key = sortedKeys.get(i);
			String value = filteredParams.get(key);
			if (i == 0) {
				stringA.append(key).append("=").append(value);
			} else {
				stringA.append("&").append(key).append("=").append(value);
			}
		}

		// Bước 4: add (private key) to end of string
		stringA.append("&key=").append(privateKey);

		// Bước 5: encrypt MD5 and convert to upperCase
		System.out.println("String A: "+ stringA + "\n");
		String signValue = md5(stringA.toString()).toUpperCase();
		return signValue;
	}

	// encrypt MD5
	public static String md5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

			// convert byte array to array hex
			StringBuilder sb = new StringBuilder();
			for (byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
