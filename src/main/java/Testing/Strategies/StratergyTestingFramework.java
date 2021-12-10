package Testing.Strategies;

import Testing.Util.AtomicResult;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

public interface StratergyTestingFramework {

    public Strategy getStratergy(BarSeries s);

    public AtomicResult getResult(BarSeries s, TradingRecord r);
}
