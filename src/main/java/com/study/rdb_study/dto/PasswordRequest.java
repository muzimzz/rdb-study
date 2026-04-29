package com.study.rdb_study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordRequest {
    String inputPassword;
    String newPassword;
}
