package FileIO;

import Testing.Strategies.MeanReversion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import util.DataStructures.Stock;
import util.DataStructures.Stocks;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ReadMeanLog {

    public static void main(String[] args) throws IOException {
        fromFile("logMeanReversion" + File.separator + ZonedDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".txt");
    }
    public static Stocks fromFile(String filename) throws IOException {
        Scanner s = new Scanner(new File(filename));
        Stocks stocks = new Stocks();
        String line;
        while (s.hasNextLine()) {
            line = s.nextLine();
            stocks.put(getStock(line));
        }
        System.out.println(stocks.allSymbols());
        return stocks;
    }

    private static Stock getStock(String line) {
        String delemiter = "( \\| )|:";
        String[] arr = line.split(delemiter);
        BarSeries series = new BaseBarSeries(arr[0]);
        double close = Double.parseDouble(arr[2].split("=")[1]);
        return new Stock(arr[0], series, new MeanReversion().getStrategy(series, close));
    }
}
