package com.example.library.dto;

import java.util.List;

public record ApiError(String message, List<String> debugMessage) {
}