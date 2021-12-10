package Testing;

import Testing.Strategies.MeanReversion;
import Twillio.Converstaion;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.asset.Asset;
import net.jacobpeterson.alpaca.model.endpoint.asset.enums.AssetStatus;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import util.DataStructures.TopKResults;
import util.LoginAlpaca;
import util.ParallelGetData;


import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestStrategy {

    public static final int MAX_SYMBOLS = 10;

    public static final AlpacaAPI api = LoginAlpaca.getLogin();

    public static final int NUM_OF_STOCKS = 100000;
    private static final Lock l = new ReentrantLock();

    public static void main(String[] args) throws AlpacaClientException {
        List<Asset> l = api.assets().get(AssetStatus.ACTIVE, "us_equity");
        long start = System.currentTimeMillis();
        MeanReversion b = new MeanReversion();
        TopKResults<MeanReversion.MeanReversionResult> result = ParallelGetData.getResult(l, b);
        String r = result.toString();
        System.out.println(r);
        long end = System.currentTimeMillis();
        Converstaion.sendMessage("\n" + "Time taken: " + (end - start) + "ms\n" + (end - start) / 1000.0 + "s\n" + (end - start) / 60000.0 + "min");
        System.out.println("Time taken: " + (end - start));
    }
}
