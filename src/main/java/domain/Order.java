package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Long customerId;
    private Long productId;
    private int quantity;
    private LocalDateTime orderDate;
    private String status;
}
