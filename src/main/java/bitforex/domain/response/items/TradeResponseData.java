package bitforex.domain.response.items;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeResponseData {

    @SerializedName("orderId")
    private String orderId;

}
