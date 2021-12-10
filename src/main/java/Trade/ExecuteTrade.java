package Trade;

import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.quote.Quote;
import net.jacobpeterson.alpaca.model.endpoint.order.Order;
import net.jacobpeterson.alpaca.model.endpoint.order.enums.OrderSide;
import net.jacobpeterson.alpaca.model.endpoint.order.enums.OrderStatus;
import net.jacobpeterson.alpaca.model.endpoint.order.enums.OrderTimeInForce;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;

import java.util.Map;

public class ExecuteTrade {

    private AlpacaAPI api;

    public ExecuteTrade() {
        String acc = PAPER_TRADING ? "PAPER": "LIVE";
        AlpacaAPI api = new AlpacaAPI(System.getenv(acc + "_KEY_ID"), System.getenv(acc + "_SECRET_KEY"));
    }

    public static final Boolean PAPER_TRADING = true;
    public static final int MAX_DOLLAR_AMOUNT = 100;

    public boolean executeAll(Map<String, Boolean> allTrades) {
        for (Map.Entry<String, Boolean> ent: allTrades.entrySet()) {
            String ticker = ent.getKey();
            if (ent.getValue()) {
                buy(ticker, MAX_DOLLAR_AMOUNT);
            } else {
                sell(ticker, MAX_DOLLAR_AMOUNT);
            }
        }
        return true;
    }

    public OrderStatus buy(String ticker, int quantity) {
        try {
            return api.orders().requestMarketOrder(ticker, quantity, OrderSide.BUY, OrderTimeInForce.FILL_OR_KILL).getStatus();
        } catch (AlpacaClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderStatus sell(String ticker, int quantity) {
        try {
            return api.orders().requestMarketOrder(ticker, quantity, OrderSide.SELL, OrderTimeInForce.FILL_OR_KILL).getStatus();
        } catch (AlpacaClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderStatus buy(String ticker, double dollarAmount) {
        return tradeFractional(ticker, dollarAmount, OrderSide.BUY);
    }

    public OrderStatus sell(String ticker, double dollarAmount) {
        return tradeFractional(ticker, dollarAmount, OrderSide.SELL);
    }

    public OrderStatus sellAll(String ticker) {
        try {
            return api.positions().close(ticker, null, 100d).getStatus();
        } catch (AlpacaClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderStatus tradeFractional(String ticker, double dollarAmount, OrderSide side) {
        Quote currQuote = null;
        try {
            currQuote = api.marketData().getLatestQuote(ticker).getQuote();
            double shares = dollarAmount / (currQuote.getAp() + currQuote.getBp() / 2);
            return api.orders().requestFractionalMarketOrder(ticker, shares, side).getStatus();
        } catch (AlpacaClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
