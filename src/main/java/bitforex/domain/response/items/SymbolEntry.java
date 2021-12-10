package bitforex.domain.response.items;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SymbolEntry {

    @SerializedName("pricePrecision")
    private int pricePrecision;

    @SerializedName("amountPrecision")
    private int amountPrecision;

    @SerializedName("minOrderAmount")
    private double minOrderAmount;

    @SerializedName("symbol")
    private double symbol;

}
