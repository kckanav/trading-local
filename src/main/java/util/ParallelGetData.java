package util;

import Testing.Strategies.StratergyTestingFramework;
import Testing.TestStrategy;
import Testing.Util.AtomicResult;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.asset.Asset;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.Bar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.BarsResponse;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.enums.BarAdjustment;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import org.ta4j.core.*;
import util.DataStructures.TopKResults;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelGetData {

    public static int K = TestStrategy.MAX_SYMBOLS;
    public static final int CUTOFF = 5;

    private static final AlpacaAPI ALPACA_API = LoginAlpaca.getLogin();

    private static final ForkJoinPool POOL = new ForkJoinPool();
    private static StratergyTestingFramework sf;

    public static <R extends AtomicResult, S extends StratergyTestingFramework> TopKResults<R> getResult(List<Asset> list, S str) {
        sf = str;
        return POOL.invoke(new ParallelGetData.Task<R>(list, 0, Math.min(TestStrategy.NUM_OF_STOCKS, list.size())));
    }

    private static <R extends AtomicResult> TopKResults<R> seq(List<Asset> l, int lo, int hi) {
        TopKResults<R> heap = new TopKResults<>(K);
        int errorCounts = 0;
        for (int i = lo; i < hi; i++) {
            try {
                BarSeries series = getData(l.get(i).getSymbol());
                if (series.isEmpty()) {
                    continue;
                }
                Strategy strategy = sf.getStratergy(series);
                BarSeriesManager manager = new BarSeriesManager(series);
                TradingRecord tradingRecord = manager.run(strategy);
                AtomicResult r = sf.getResult(series, tradingRecord);
                if (r.shouldInclude()) {System.out.println(l.get(i).getSymbol());}
                heap.add(r);
            } catch (Exception e) {
                e.printStackTrace();
                errorCounts++;
            }
        }
        heap.errorCounts = errorCounts;
        return heap;
    }

    private static class Task<R extends AtomicResult> extends RecursiveTask<TopKResults<R>> {

        private final List<Asset> l;
        private final int lo;
        private final int hi;

        public Task(List<Asset> l, int lo, int hi) {
            this.l = l;
            this.lo = lo;
            this.hi = hi;
        }
        @Override
        protected TopKResults<R> compute() {
            if (hi - lo <= CUTOFF) {
                return seq(l, lo, hi);
            }

            Task<R> left = new Task<>(l, lo, (hi + lo) / 2);
            Task<R> r = new Task<>(l, (hi + lo) / 2, hi);
            left.fork();
            return r.compute().combine(left.join());
        }
    }

    /**
     * Uses the AlpacaApI to get the market data for ticker, as candle bars.
     * @param ticker the stock's ticker name
     * @return BarSeries which contains all the candle bars.
     */
    private static BarSeries getData(String ticker) throws AlpacaClientException {

        BarSeries series = new BaseBarSeriesBuilder().withName(ticker).build();
        BarsResponse aaplBarsResponse = ALPACA_API.marketData().getBars(
                ticker,
                ZonedDateTime.now().minusDays(400),
                ZonedDateTime.now(),
                400,
                null,
                1,
                BarTimePeriod.DAY,
                BarAdjustment.SPLIT);
        if (aaplBarsResponse.getBars() == null) {return series;}
        for (Bar b: aaplBarsResponse.getBars()) {
            series.addBar(b.getT(), b.getO(), b.getH(), b.getL(), b.getC(), b.getV());
        }
        return series;
    }
}
