package bitforex.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelOrderRequest {
    private String orderId;
    private String base;
    private String quote;
}
