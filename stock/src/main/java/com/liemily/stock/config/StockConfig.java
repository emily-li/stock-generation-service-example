package com.liemily.stock.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by Emily Li on 24/08/2017.
 */
@Configuration
@EntityScan("com.liemily.stock")
@Lazy
public class StockConfig {
}
