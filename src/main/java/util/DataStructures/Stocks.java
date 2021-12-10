package util.DataStructures;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Stocks {

    private ConcurrentHashMap<String, Stock> map;

    public Stocks() {
        map = new ConcurrentHashMap<>();
    }

    public Stock get(String symbol) {
        return map.get(symbol);
    }

    public Stock put(Stock s) {
        return map.put(s.symbol, s);
    }

    public int size() {
        return map.size();
    }

    public Set<String> allSymbols() {
        return map.keySet();
    }

}


