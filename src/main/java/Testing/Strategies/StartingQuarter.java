package Testing.Strategies;

import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.Bar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.BarsResponse;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.enums.BarAdjustment;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import org.ta4j.core.*;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;


// TODO: Use snapshot to get the last day close, instead of calculating on your own.
public class StartingQuarter {

    private AlpacaAPI api;

    public void run(String ticker, AlpacaAPI api) {

    }

    private Double getLastClose(String ticker, int day, int month, int year) throws AlpacaClientException {
        List<Bar> bars = api.marketData().getBars(
                ticker,
                ZonedDateTime.of(year, month, day, 3, 58, 0, 0, ZoneId.of("America/New_York")),
                ZonedDateTime.of(year, month, day, 4, 0, 0, 0, ZoneId.of("America/New_York")),
                100, null, 1,
                BarTimePeriod.MINUTE, BarAdjustment.SPLIT).getBars();
        return bars.get(bars.size() - 1).getC();
    }

    private Double getYesterdayClose(String ticker) throws AlpacaClientException {
        int day = ZonedDateTime.now().minusDays(1).getDayOfMonth();
        int month = ZonedDateTime.now().minusDays(1).getMonthValue();
        int year = ZonedDateTime.now().minusDays(1).getYear();
        return getLastClose(ticker, day, month, year);
    }

    private double getTodayFirst15Min(String ticker) throws AlpacaClientException {
        int day = ZonedDateTime.now().getDayOfMonth();
        int month = ZonedDateTime.now().getMonthValue();
        int year = ZonedDateTime.now().getYear();
        return getFirst15min(ticker, day, month, year);
    }

    private double getFirst15min(String ticker, int day, int month, int year) throws AlpacaClientException {
        BarsResponse bars = api.marketData().getBars(
                ticker,
                ZonedDateTime.of(year, month, day, 9, 30, 0, 0, ZoneId.of("America/New_York")),
                ZonedDateTime.of(year, month, day, 9, 45, 0, 0, ZoneId.of("America/New_York")),
                100, null, 1,
                BarTimePeriod.MINUTE, BarAdjustment.SPLIT);
        double max = 0;
        for (Bar b: bars.getBars()) {
            if (b.getH() > max) {
                max = b.getH();
            }
        }
        return max;
    }

    public Strategy getStratergy(String ticker, BarSeries s) {
        try {
            Double lastClose = getYesterdayClose(ticker);
            ClosePriceIndicator closePrice = new ClosePriceIndicator(s);


            MACDIndicator macdIndicator = new MACDIndicator(closePrice);

            double max = getTodayFirst15Min(ticker);

            Rule buy = (new CrossedUpIndicatorRule(closePrice, lastClose * 1.04)
                    .or(new CrossedUpIndicatorRule(closePrice, max)))
                    .and(new CrossedUpIndicatorRule(macdIndicator, 0));

            Rule sell = new StopLossRule(closePrice, 3.0)
                    .or(new StopGainRule(closePrice, 2.0));

            return new BaseStrategy(buy, sell);
        } catch (AlpacaClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
