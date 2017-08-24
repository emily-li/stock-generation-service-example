package com.liemily.stockgeneration.config;

import com.liemily.dataaccess.config.DataAccessConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * Created by Emily Li on 21/08/2017.
 */
@Configuration
@ComponentScan("com.liemily.stockgeneration")
@Import(DataAccessConfig.class)
@Lazy
public class StockGenerationServiceConfig {
}
