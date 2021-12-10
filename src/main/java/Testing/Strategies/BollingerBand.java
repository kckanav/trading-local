package Testing.Strategies;

import Testing.Util.AtomicResult;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.StopGainRule;
import org.ta4j.core.rules.StopLossRule;

public class BollingerBand implements StratergyTestingFramework {
    private final int smallBarCount;
    private final int largeBarCount;


    public BollingerBand(int smallBarCount, int largeBarCount) {
        this.smallBarCount = smallBarCount;
        this.largeBarCount = largeBarCount;
    }

    @Override
    public Strategy getStratergy(BarSeries s) {
        return getStrategyWithParams(s, smallBarCount, largeBarCount);
    }

    public AtomicResult getResult(BarSeries s, TradingRecord r) {
        return new PLRatioResult(r.getName(), analysis(s, r));
    }

    private Num analysis(BarSeries series, TradingRecord record) {
        AnalysisCriterion c1 = new GrossProfitCriterion();
        return c1.calculate(series, record);
    }

    /**
     * Creates the strategy we want to trade on.
     * Cuurent Strategy is bollinger bands.
     * @param series The series we need to get the strategy for
     * @return Strategy that we follow.
     */
    public Strategy getStrategyWithParams(BarSeries series, int barCountSmall, int barCountLarge) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator shortSma = new SMAIndicator(closePrice, barCountSmall);

        // Getting a longer SMA (e.g. over the 30 last ticks)
        SMAIndicator longSma = new SMAIndicator(closePrice, barCountLarge);

        // Buying rules
        // We want to buy:
        //  - if the 5-ticks SMA crosses over 30-ticks SMA
        //  - or if the price goes below a defined price (e.g $800.00)
        Rule buyingRule = new CrossedUpIndicatorRule(shortSma, longSma)
                .or(new CrossedDownIndicatorRule(closePrice, 800d));

        // Selling rules
        // We want to sell:
        //  - if the 5-ticks SMA crosses under 30-ticks SMA
        //  - or if the price loses more than 3%
        //  - or if the price earns more than 2%
        Rule sellingRule = new CrossedDownIndicatorRule(shortSma, longSma)
                .or(new StopLossRule(closePrice, 3.0))
                .or(new StopGainRule(closePrice, 2.0));

        return new BaseStrategy(buyingRule, sellingRule);
    }

    public static class PLRatioResult implements AtomicResult {

        public String symbol;
        public Num result;
        public boolean shouldAdd;


        public PLRatioResult(String symbol, Num result) {
            this.result = result;
            this.symbol = symbol;
        }

        public String toString() {
            return symbol + ": " + result.toString();
        }

        @Override
        public boolean shouldInclude() {
            return shouldAdd;
        }

        @Override
        public String symbol() {
            return symbol;
        }

        @Override
        public int compareTo(AtomicResult o) {
            if (!(o instanceof PLRatioResult)) {
                throw new IllegalArgumentException("Requires a AnalysisResult object");
            }
            PLRatioResult temp = (PLRatioResult) o;
            return result.compareTo(temp.result);
        }
    }
}
