package com.example.univeus.domain.auth.strategy;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class SecretRandomPublisher implements CertificationNumberPublisher {

    private static final Integer RANGE_START = 100000;
    private static final Integer RANGE_END = 900000;


    @Override
    public Integer publish() {
        SecureRandom secureRandom = new SecureRandom();
        return RANGE_START + secureRandom.nextInt(RANGE_END);
    }
}
