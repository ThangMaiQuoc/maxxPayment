package com.shop.bike.consumer.rest;

import com.shop.bike.entity.enumeration.PaymentParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class MaxxPaymentConsumerResource {

	@GetMapping("/public")
	public String home(Model model, @RequestParam PaymentParam paymentParam) {
		model.addAttribute("message", "Welcome to the Spring Boot application!");
		if (paymentParam.equals(PaymentParam.SC_SUCCESSURL))
			return "paymentSuccess"; // Tên của file HTML bạn muốn trả về
		else if (paymentParam.equals(PaymentParam.SC_FAILURL)) {
			return "paymentFail";
		}
		else {
			return "paymentCancel";
		}
	}
}