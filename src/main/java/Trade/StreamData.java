package Trade;

import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.realtime.enums.MarketDataMessageType;
import net.jacobpeterson.alpaca.websocket.marketdata.MarketDataListener;
import util.LoginAlpaca;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StreamData {

    public static final AlpacaAPI alpacaAPI = LoginAlpaca.getLogin();

    public static void main(String[] args) {


    }

    public static void startStream(Set<String> symbol, MarketDataListener listener) {
        System.out.println("Starting Stream for " + symbol.toString());
        // Add a 'MarketDataListener' that simply prints market data information
        alpacaAPI.marketDataStreaming().setListener(listener);

        // Listen to 'SubscriptionsMessage', 'SuccessMessage', and 'ErrorMessage' control messages
        // that   contain information about the stream's current state. Note that these are subscribed
        // to before the websocket is connected since these messages usually are sent
        // upon websocket connection.
        alpacaAPI.marketDataStreaming().subscribeToControl(
                MarketDataMessageType.SUCCESS,
                MarketDataMessageType.SUBSCRIPTION,
                MarketDataMessageType.ERROR);

        // Connect the websocket and confirm authentication
        alpacaAPI.marketDataStreaming().connect();
        alpacaAPI.marketDataStreaming().waitForAuthorization(5, TimeUnit.SECONDS);
        if (!alpacaAPI.marketDataStreaming().isValid()) {
            System.out.println("Websocket not valid!");
            return;
        }

        // Listen to the AAPL and TSLA trades and all ('*') bars.
        alpacaAPI.marketDataStreaming().subscribe(
                symbol,
                null,
                symbol);

        // Wait a few seconds
        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Manually disconnect the websocket
        alpacaAPI.marketDataStreaming().disconnect();
    }
}
