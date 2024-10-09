package com.shop.bike.consumer.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestBossPaymentDTO {
    
    private String amount = "100";
    
    private String mchOrderNo = "mho1725423431189";

    private String subject = "Product Title";

    private String wayCode = "SM_GCash";

    private String reqTime = "1725471225207";

    private String body = "Product Description";

    private String version = "1.0";

    private String appId = "67056801e4b06e0a57de66b2";

    private String notifyUrl = "https://www.bosspay.com/notify.html";

    private String signType = "MD5";

    private String currency = "PHP";

    private String mchNo = "M1728407553";
}
