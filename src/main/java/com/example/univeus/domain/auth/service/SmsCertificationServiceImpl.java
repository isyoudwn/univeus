package com.example.univeus.domain.auth.service;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.common.util.CoolSmsUtil;
import com.example.univeus.domain.auth.exception.AuthException;
import com.example.univeus.domain.auth.repository.SmsCertificationDao;
import com.example.univeus.domain.auth.strategy.CertificationNumberPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {

    private final CoolSmsUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;
    private final CertificationNumberPublisher certificationNumberPublisher;

    @Override
    public void issueCertificationSms(String phone) {
        String code = certificationNumberPublisher.publish().toString();
        smsCertificationDao.deleteByKey(phone);

        smsUtil.sendSms(phone, code);
        smsCertificationDao.save(phone, code);
    }


    @Override
    public void verifyNumber(String expectedCode, String phoneNumber) {

        String actualCode = smsCertificationDao.getByKey(phoneNumber);

        if (actualCode == null) {
            throw new AuthException(CERTIFICATION_BAD_REQUEST);
        }

        if (!actualCode.equals(expectedCode)) {
            throw new AuthException(INVALID_CERTIFICATION_NUMBER);
        }
    }
}
