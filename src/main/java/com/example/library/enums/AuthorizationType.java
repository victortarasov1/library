package com.example.library.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorizationType {
    BEARER("Bearer ");
    private final String prefix;
}
