package com.example.univeus.domain.auth.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Accessor {
    private static final Long GUEST_ID = 0L;

    private final AccessorRole accessorRole;
    private final Long memberId;

    public static Accessor member(Long memberId) {
        return Accessor.of(AccessorRole.MEMBER, memberId);
    }

    public static Accessor guest() {
        return Accessor.of(AccessorRole.GUEST, GUEST_ID);
    }

    public static Accessor admin(Long memberId) {
        return Accessor.of(AccessorRole.ADMIN, memberId);
    }

    public static Accessor of(AccessorRole accessRole, Long memberId) {
        return new Accessor(accessRole, memberId);
    }
}
