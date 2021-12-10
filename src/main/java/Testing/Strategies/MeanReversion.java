package Testing.Strategies;

import Testing.Util.AtomicResult;
import org.ta4j.core.*;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.StopGainRule;
import org.ta4j.core.rules.StopLossRule;

public class MeanReversion implements StratergyTestingFramework {

    private static final int DMA_BARS = 200;
    private static final int GROWTH_RATE_BARS = 5;
    private static final int RSI_BARS = 2;
    private static final int RSI_THRESHOLD = 50;
    private static final double DAY2_INCREASE = 1.03;

    // TODO: Get data and find all those that match the specfic crtieron each day.
    // TODO: Filter all those that match the criteora to 10 based on certain aspects.
    // TODO: Implement the Buy rule and the sell rule.

    // The bar series should be for 1 day intervals, and must contain at least 200 bars.
    @Override
    public Strategy getStratergy(BarSeries s) {

        ClosePriceIndicator close = new ClosePriceIndicator(s);

        Num lastClose = close.getValue(s.getEndIndex());

        Rule sell = new CrossedUpIndicatorRule(close, lastClose.multipliedBy(lastClose.numOf(1.01)));

        Rule buy = new StopLossRule(close, 6).
                or(new StopGainRule(close, 3));

        return new BaseStrategy(sell, buy);
    }

    public Strategy getStrategy(BarSeries s, double lastClose) {

        ClosePriceIndicator close = new ClosePriceIndicator(s);

        Rule sell = new CrossedUpIndicatorRule(close, lastClose * 1.01);

        Rule buy = new StopLossRule(close, 6).
                or(new StopGainRule(close, 3));

        return new BaseStrategy(sell, buy);
    }

    @Override
    public AtomicResult getResult(BarSeries s, TradingRecord r) {
        return new MeanReversionResult(s, r);
    }


    public static class MeanReversionResult implements AtomicResult {
        private BarSeries s;
        private TradingRecord r;
        private boolean shouldInclude;
        private Num growthRate;
        private String toString;

        public MeanReversionResult(BarSeries s, TradingRecord r) {
            this.s = s;
            this.r = r;
            setGrowthRate();
            setShouldInclude();
        }

        private void setGrowthRate() {
            ClosePriceIndicator close = new ClosePriceIndicator(s);
            Num day1 = close.getValue(s.getEndIndex() - GROWTH_RATE_BARS + 1);
            Num curr = close.getValue(s.getEndIndex());
            growthRate = curr.minus(day1).dividedBy(day1);
        }

        private void setShouldInclude() {
            ClosePriceIndicator close = new ClosePriceIndicator(s);
            Num lastDma = new SMAIndicator(close, DMA_BARS).getValue(s.getEndIndex());
            Num lastClose = close.getValue(s.getEndIndex());
            Num dayBeforeYesClose = close.getValue(s.getEndIndex() - 1);
            Num lastRsi = new RSIIndicator(close, RSI_BARS).getValue(s.getEndIndex());
            setString(lastDma, lastClose, dayBeforeYesClose, lastRsi);
            shouldInclude =  lastClose.isGreaterThan(lastDma)
                    && lastRsi.isGreaterThanOrEqual(lastRsi.numOf(RSI_THRESHOLD))
                    && lastClose.isGreaterThanOrEqual(dayBeforeYesClose.multipliedBy(dayBeforeYesClose.numOf(DAY2_INCREASE)));
        }

        @Override
        public int compareTo(AtomicResult o) {
            if (!(o instanceof MeanReversionResult)) {
                throw new IllegalArgumentException("Argument must be a MeanReversionResult");
            }
            MeanReversionResult other = (MeanReversionResult) o;
            return growthRate.compareTo(other.growthRate);
        }

        @Override
        public boolean shouldInclude() {
            return shouldInclude;
        }

        private void setString(Num dma, Num lastClose, Num dayBefore, Num rsi) {
            toString = s.getName() + ": DMA over " + DMA_BARS + " bars = " + dma + " | " + "Last Close = " + lastClose + " | " +
                    "Day before yesterday close = " + dayBefore + " | " + "RSI over " + RSI_BARS + " bars = " + rsi + " | " + "Growth Rate over " +
            GROWTH_RATE_BARS + " bars = " + growthRate;
        }

        public String symbol() {
            return s.getName();
        }

        public String toString(){
            return toString;
        }
    }
}
