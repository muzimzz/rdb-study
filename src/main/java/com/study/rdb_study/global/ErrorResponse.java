package com.study.rdb_study.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

// public record ErrorResponse(String message, int status) { }

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
}
