package com.shop.bike.consumer.vm;

import lombok.Data;

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