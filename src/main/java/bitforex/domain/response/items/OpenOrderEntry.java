package bitforex.domain.response.items;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OpenOrderEntry {

    @SerializedName("createTime")
    private Long createTime;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("tradeType")
    private Integer tradeType;

}
