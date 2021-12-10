package bitforex.domain.response.items;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SymbolsResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private SymbolEntry[] data;
}
