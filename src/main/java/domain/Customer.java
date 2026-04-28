package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Customer {
    private Long customerId;
    private String name;
    private String email;
    private String password;
    private String address;
    private LocalDateTime joinDate;
}
