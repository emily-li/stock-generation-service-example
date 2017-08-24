package com.liemily.stockgeneration;

import com.liemily.stock.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Emily Li on 24/08/2017.
 */
@Component
@Lazy
public class StockGenerator {
    private double minValue;
    private double maxValue;
    private int minVol;
    private int maxVol;

    @Autowired
    public StockGenerator(@Value("${stockupdater.generator.value.min}") double minValue,
                          @Value("${stockupdater.generator.value.max}") double maxValue,
                          @Value("${stockupdater.generator.vol.min}") int minVol,
                          @Value("${stockupdater.generator.vol.max}") int maxVol) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minVol = minVol;
        this.maxVol = maxVol;
    }

    Stock generateStock(String stockSymbol) {
        double value = generateValue(minValue, maxValue);
        int volume = generateVolume(minVol, maxVol);
        return new Stock(stockSymbol, BigDecimal.valueOf(value), volume);
    }

    private double generateValue(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private int generateVolume(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
