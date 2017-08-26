package com.liemily.stockgeneration;

import com.liemily.dataaccess.HibernateDAL;
import com.liemily.stock.Stock;
import com.liemily.stockgeneration.exceptions.StockModulationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Emily Li on 24/08/2017.
 */
@Component
public class HibernateStockGenerationService implements StockGenerationService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private HibernateDAL hibernateDAL;
    private ScheduledExecutorService scheduledExecutorService;
    private StockGenerator stockGenerator;
    private int rateMs;

    @Autowired
    public HibernateStockGenerationService(HibernateDAL hibernateDAL,
                                           ScheduledExecutorService scheduledExecutorService,
                                           StockGenerator stockGenerator,
                                           @Value("${stockupdater.modulation.rateMs}") int rateMs) {
        this.hibernateDAL = hibernateDAL;
        this.scheduledExecutorService = scheduledExecutorService;
        this.stockGenerator = stockGenerator;
        this.rateMs = rateMs;
    }

    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                modulateStocks();
            } catch (StockModulationException e) {
                logger.info("Failed to modulate stocks", e);
            }
        }, 0, rateMs, TimeUnit.MILLISECONDS);
    }

    private void modulateStocks() throws StockModulationException {
        try {
            Collection<Stock> stocks = hibernateDAL.query("FROM Stock");
            for (Stock stock : stocks) {
                double newValue = stockGenerator.modulateValue(stock.getValue().doubleValue());
                stock.setValue(BigDecimal.valueOf(newValue));
            }
            hibernateDAL.save(stocks);
        } catch (InterruptedException e) {
            String msg = "Failed to modulate stocks";
            logger.info(msg, e);
            throw new StockModulationException(msg, e);
        }
    }
}
