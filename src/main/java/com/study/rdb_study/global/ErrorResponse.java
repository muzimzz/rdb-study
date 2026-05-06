package com.study.rdb_study.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

// public record ErrorResponse(String message, int status) { }

// https://velog.io/@kiiiyeon/%EC%8A%A4%ED%94%84%EB%A7%81-ExceptionHandler%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
}
