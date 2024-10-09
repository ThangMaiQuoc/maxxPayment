package com.shop.bike.consumer.dto;

import lombok.Data;

@Data
public class RequestTransferBossPaymentDTO {

    private String mchNo = "M1728407553";
    private String appId = "67056801e4b06e0a57de66b2";
    private String mchOrderNo = "mho1725423431187";
    private String wayCode = "NB_QRPH";
    private String entryType = "BANK_CARD";
    private Integer amount = 100;
    private String currency = "PHP";
    private String accountNo = "09568099249";
    private String accountName = "Residy Torres";
    private String bankName = "GXCHPHM2XXX";
    private String clientIp = "210.73.10.122";
    private String transferDesc = "transferDesc";
    private String notifyUrl = "https://www.bosspay.com/notify.html";
    private String extParam = "134586944573118714";
    private String reqTime = "reqTime";
    private String version = "1.0";
    private String signType = "MD5";
}
