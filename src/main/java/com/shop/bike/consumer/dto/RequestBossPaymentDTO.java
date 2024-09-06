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

    private String wayCode = "PH_SM";

    private String reqTime = "1725471225207";

    private String body = "Product Description";

    private String version = "1.0";

    private String appId = "66bca66fe4b0fb8d90a008f9";

    private String notifyUrl = "https://www.bosspay.com/notify.html";

    private String signType = "MD5";

    private String currency = "PHP";

    private String mchNo = "M1723639407";
}
