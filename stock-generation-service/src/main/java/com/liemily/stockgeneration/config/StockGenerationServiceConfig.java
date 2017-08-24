package com.liemily.stockgeneration.config;

import com.liemily.dataaccess.config.DataAccessConfig;
import org.springframework.context.annotation.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Emily Li on 21/08/2017.
 */
@Configuration
@ComponentScan("com.liemily.stockgeneration")
@Import(DataAccessConfig.class)
@Lazy
public class StockGenerationServiceConfig {
    @Bean
    protected ScheduledExecutorService scheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
