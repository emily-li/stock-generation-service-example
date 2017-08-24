package com.liemily.stockgeneration;

import com.liemily.dataaccess.HibernateDAL;
import com.liemily.stock.Stock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Emily Li on 24/08/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockGenerationServiceTest {
    @Autowired
    private HibernateDAL hibernateDAL;

    @Test
    public void testStockValueModulatesOverTime() throws Exception {
        final String symbol = "SYM" + UUID.randomUUID();
        BigDecimal initialValue = BigDecimal.valueOf(1.23);

        hibernateDAL.save(new Stock(symbol, initialValue, 1));
        Thread.sleep(1 * 1000);
        Stock alteredStock = (Stock) hibernateDAL.query("FROM Stock WHERE symbol='" + symbol + "'").get(0);
        BigDecimal alteredValue = alteredStock.getValue();

        Assert.assertNotEquals(alteredValue, initialValue);
    }
}
