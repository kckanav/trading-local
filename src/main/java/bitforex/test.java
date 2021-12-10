package bitforex;

import bitforex.domain.request.TradeRequest;
import bitforex.sign.ApiKey;

import java.io.IOException;
import java.math.BigDecimal;

public class test {
    public static void main(String[] args) {
        ApiKey key = new ApiKey();
        key.setKey("80f3336dd988eba16527e8b56414160c");
        key.setSecret("779405131ba2c8ae9f7cbb4dd3174798");

        BitforexRestApi api = new BitforexRestApi(key);
        try {
            System.out.println(api.getBalance());
            TradeRequest r = new TradeRequest();
            System.out.println(api.getOpenOrders("eth", "btc"));
//            r.setBase("eth");
//            r.setQuote("btc");
//            r.setPrice(new BigDecimal(0.087));
//            r.setVolume(new BigDecimal(0.001));
//            r.setIsBuy(true);
//            System.out.println(api.trade(r).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
