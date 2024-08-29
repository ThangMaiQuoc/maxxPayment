package com.shop.bike.consumer.vm;

import com.shop.bike.entity.enumeration.DiscountType;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class MessageVM {

	private Long id;

	private String senderId;

	private String receiptId;

	private String message;

	private Boolean read;

	private Integer chatId;

	private Instant createdDate;

	private String senderName;

	private String receiptName;

	private String status;

}