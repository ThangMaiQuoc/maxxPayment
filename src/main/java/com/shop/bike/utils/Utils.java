package com.shop.bike.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PHONE_PATTERN = "^[0-9]{10,15}$";

    public static boolean isValidateEmail(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidatePhone(final String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static String generateOtp() {
        Random random = new Random();
        // Tạo chữ số đầu tiên trong khoảng từ 1 đến 9
        int firstDigit = random.nextInt(9) + 1;
        // Tạo các chữ số còn lại trong khoảng từ 0 đến 9
        int remainingDigits = random.nextInt(100000); // 5 chữ số còn lại

        return String.format("%d%05d", firstDigit, remainingDigits);
    }
}