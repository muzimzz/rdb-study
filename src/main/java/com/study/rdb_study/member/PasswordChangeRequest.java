package com.study.rdb_study.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordChangeRequest {
    private String inputPassword;
    private String newPassword;
}
