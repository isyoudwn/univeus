package com.example.univeus.presentation.member.controller;

import static com.example.univeus.common.response.ResponseMessage.UPDATE_PHONE_NUMBER_SUCCESS;
import static com.example.univeus.presentation.member.dto.request.MemberRequest.*;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.common.annotation.MemberOnly;
import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/v1/member"))
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @MemberOnly
    @PatchMapping("/phone")
    public ResponseEntity<Response<String>> updatePhoneNumber(
            @Auth Accessor accessor,
            @RequestBody @Valid PhoneNumber phoneNumberRequest
    ) {
        Long memberId = accessor.getMemberId();
        memberService.updatePhoneNumber(memberId, phoneNumberRequest.phoneNumber());

        return ResponseEntity
                .ok()
                .body(Response.success(
                        UPDATE_PHONE_NUMBER_SUCCESS.getCode(),
                        UPDATE_PHONE_NUMBER_SUCCESS.getMessage()
                ));
    }
}
