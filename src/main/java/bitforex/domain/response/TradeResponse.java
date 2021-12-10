package bitforex.domain.response;

import bitforex.domain.response.items.TradeResponseData;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeResponse{

    @SerializedName("success")
    private Boolean success;

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private TradeResponseData data;

}
