package util.DataStructures;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

public class Stock {

    public final String symbol;
    public final BarSeries series;
    public final Strategy strategy;

    public Stock(String symbol, BarSeries series, Strategy strategy) {
        this.symbol = symbol;
        this.series = series;
        this.strategy = strategy;
    }

    public boolean shouldEnter() {
        return strategy.shouldEnter(series.getEndIndex());
    }

    public boolean shouldExit() {
        return strategy.shouldExit(series.getEndIndex());
    }

}