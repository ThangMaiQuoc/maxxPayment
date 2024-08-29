package com.shop.bike.service.dto;

import com.shop.bike.entity.enumeration.MobileNetworksType;
import lombok.Data;
import javax.annotation.Nullable;
import java.math.BigDecimal;

@Data
public class UploadInformationDTO {


	private Long id;

	private String phone;


	private BigDecimal totalMoney;

	private String image;
	
	private MobileNetworksType mobileNetworksType;
	
	private String companyName;
	
	private String userName;
}
