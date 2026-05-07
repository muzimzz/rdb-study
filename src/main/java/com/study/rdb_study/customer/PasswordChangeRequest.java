package com.study.rdb_study.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordChangeRequest {
    String inputPassword;
    String newPassword;
}
