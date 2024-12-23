package com.example.univeus.domain.auth;

import com.example.univeus.common.exception.ErrorCode;
import com.example.univeus.domain.auth.exception.AuthException;
import com.example.univeus.domain.auth.model.Accessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MemberOnlyChecker {
    @Before("@annotation(MemberOnly)")
    public void check(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Accessor accessor && !accessor.isMember()) {
                throw new AuthException(ErrorCode.AUTH_BAD_REQUEST);
            }
        }
    }
}
