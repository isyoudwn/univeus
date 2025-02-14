package com.example.univeus.domain.auth.service;

public interface SmsCertificationService {

    void issueCertificationSms(String phone);

    void verifyNumber(String expectedCode, String phoneNumber);
}
