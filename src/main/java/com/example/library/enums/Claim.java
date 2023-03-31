package com.example.library.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Claim {
    ROLES("roles");
    private final String claim;
}
