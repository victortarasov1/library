package com.example.library.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Token {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");
    private final String tokenType;
}
