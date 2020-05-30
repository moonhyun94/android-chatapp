package com.example.chat_app.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidationUtils {

    private String name, email, password, phoneNum;
    private static String psw = "^(?=.*\\d)(?=.*[\\W])(?=.*[a-z])(?=.*[A-Z]).{8,15}$";
    private static String phone = "^01(?:0|1[6-9])(\\d{4})(\\d{4})$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(psw);
    private static final Pattern PHONE_PATTERN = Pattern.compile(phone);

    public ValidationUtils() {}

    public ValidationUtils(String name, String email, String password, String phoneNum) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public boolean isOk() {
        if(!name.isEmpty() &&
                (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
                (!phoneNum.isEmpty() && PHONE_PATTERN.matcher(phoneNum).matches()) &&
                (!password.isEmpty() && PASSWORD_PATTERN.matcher(password).matches())) {
            return true;
        } else {
            return false;
        }
    }

    public static Pattern getPasswordPattern() {
        return PASSWORD_PATTERN;
    }

    public static Pattern getPhonePattern() {
        return PHONE_PATTERN;
    }
}
