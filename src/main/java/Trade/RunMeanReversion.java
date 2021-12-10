package Trade;

import FileIO.ReadMeanLog;
import FileIO.StateGenerator;
import Testing.Strategies.MeanReversion;
import Testing.Util.AtomicResult;
import Twillio.Converstaion;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.asset.Asset;
import net.jacobpeterson.alpaca.model.endpoint.asset.enums.AssetStatus;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.snapshot.Snapshot;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.realtime.MarketDataMessage;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.realtime.bar.BarMessage;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.realtime.enums.MarketDataMessageType;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.realtime.trade.TradeMessage;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import net.jacobpeterson.alpaca.websocket.marketdata.MarketDataListener;
import org.ta4j.core.*;
import util.DataStructures.Stock;
import util.DataStructures.Stocks;
import util.DataStructures.TopKResults;
import util.LoginAlpaca;
import util.ParallelGetData;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class RunMeanReversion {

    private static final AlpacaAPI alpacaApi = LoginAlpaca.getLogin();

    private Stocks allStock;

    public static void main(String[] args) {
        try {
            List<Asset> l = alpacaApi.assets().get(AssetStatus.ACTIVE, "us_equity");
            RunMeanReversion r = new RunMeanReversion();
            //r.setup(l);
            r.setupFromFile("logMeanReversion" + File.separator + ZonedDateTime.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE) + ".txt");
            r.addBar(null);
            //r.start();
        } catch (AlpacaClientException e) {
            e.printStackTrace();
        }
    }

    public void setup(List<Asset> l) throws AlpacaClientException {
        StateGenerator gen = new StateGenerator(ZonedDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        allStock = new Stocks();
        MeanReversion b = new MeanReversion();
        TopKResults<MeanReversion.MeanReversionResult> stockToWatch = ParallelGetData.getResult(l, b);
        Map<String, Snapshot> map = alpacaApi.marketData().getSnapshots(stockToWatch.getAllSymbols());
        BarSeries temp;
        Stock stock;
        String s = "";
        for (AtomicResult r: stockToWatch) {
            temp = new BaseBarSeries(r.symbol());
            Snapshot snap = map.get(r.symbol());
            if (snap == null) {
                String se = "Snapshot for " + r.symbol() + " is not available";
                System.out.println(se);
                s += se + "\n";
                continue;
            }
            stock = new Stock(r.symbol(), temp, new MeanReversion().getStrategy(temp, snap.getDailyBar().getC()));
            allStock.put(stock);
            s += r.toString() + "\n";
        }
        gen.write(s);
        StateGenerator.genTextFile(ZonedDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    public void setupFromFile(String filename) {
        try {
            allStock = ReadMeanLog.fromFile(filename);
            // start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        StreamData.startStream(allStock.allSymbols(), new Action());
    }

    private void addBar(BarMessage m) {
        //String ticker = m.getSymbol();
        String ticker = "MRVL";

        BarSeries temp = allStock.get(ticker).series;
        //temp.addBar(m.getTimestamp(), m.getOpen(), m.getHigh(), m.getLow(), m.getClose(), m.getVolume());
        temp.addBar(ZonedDateTime.now(), 100, 100, 50, 100, 100);

        print(temp);
        System.out.println(allStock.get(ticker).strategy.getEntryRule());
        if (allStock.get(ticker).strategy.shouldEnter(temp.getEndIndex())) {
            System.out.println("Here ");
        }
        if (allStock.get(ticker).shouldEnter()) {
            System.out.println("Should enter " + ticker + " at ");
        } else if (allStock.get(ticker).shouldExit()) {
            System.out.println("Should exit " + ticker + " at ");
        }
    }

    private void checkTrade(TradeMessage message) {
        String ticker = message.getSymbol();
        // TODO: Implement trade check and execute.
    }

    private class Action implements MarketDataListener {
        private int c = 0;

        @Override
        public void onMessage(MarketDataMessageType messageType, MarketDataMessage message) {
            Thread t = new Thread() {
                public void run() {
                    //System.out.println(message.toString());
                    switch (messageType) {
                        case BAR:
                            addBar((BarMessage) message);
                            break;
                        case ERROR:
                            Converstaion.sendMessage(message.toString());
                            break;
                        case SUCCESS:
                            Converstaion.sendMessage(message.toString());
                            break;
                        case TRADE:
                            checkTrade((TradeMessage) message);
                            break;
                    }
                }
            };
            t.start();
            c++;
            System.out.println(c);
        }
    }

    private void print(BarSeries s) {
        String str = "Bar for " + s.getName() + " [";
        for (int i = 0 ; i <= s.getEndIndex(); i++) {
            str += s.getBar(i).toString() + "] | ";
        }
        System.out.println(str);
    }
}
