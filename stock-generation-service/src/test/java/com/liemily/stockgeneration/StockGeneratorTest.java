package com.liemily.stockgeneration;

import com.liemily.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Emily Li on 24/08/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockGeneratorTest {
    @Autowired
    private StockGenerator stockGenerator;

    @Value("${stockupdater.generator.value.min}")
    private double minValue;
    @Value("${stockupdater.generator.value.max}")
    private double maxValue;
    @Value("${stockupdater.generator.vol.min}")
    private int minVol;
    @Value("${stockupdater.generator.vol.max}")
    private int maxVol;

    private String stockSymbol = "SYM";
    private Stock generatedStock;

    @Before
    public void setup() {
        generatedStock = stockGenerator.generateStock(stockSymbol);
    }

    @Test
    public void testGenerateStockValue() {
        double generatedStockValue = generatedStock.getValue().doubleValue();

        assertTrue(generatedStockValue >= minValue);
        assertTrue(generatedStockValue <= maxValue);
    }

    @Test
    public void testGeneratedStockValueChanges() {
        Stock newGeneratedStock = stockGenerator.generateStock(stockSymbol);

        assertNotEquals(generatedStock.getValue(), newGeneratedStock.getValue());
    }

    @Test
    public void testGenerateStockVolume() {
        int generatedStockValue = generatedStock.getVolume();

        assertTrue(generatedStockValue >= minVol);
        assertTrue(generatedStockValue <= maxVol);
    }

    @Test
    public void testGeneratedStockVolumeChanges() {
        Stock newGeneratedStock = stockGenerator.generateStock(stockSymbol);

        assertNotEquals(generatedStock.getVolume(), newGeneratedStock.getVolume());
    }


    @Test
    public void testValueModulation() {
        double initialValue = generatedStock.getValue().doubleValue();
        double modulatedValue = stockGenerator.modulateValue(initialValue);
        assertNotEquals(modulatedValue, initialValue);
    }

    @Test
    public void testValueModulationRandomised() {
        double initialValue = generatedStock.getValue().doubleValue();
        double modulatedValue1 = stockGenerator.modulateValue(initialValue);
        double modulatedValue2 = stockGenerator.modulateValue(initialValue);
        assertNotEquals(modulatedValue1, modulatedValue2);
    }
}