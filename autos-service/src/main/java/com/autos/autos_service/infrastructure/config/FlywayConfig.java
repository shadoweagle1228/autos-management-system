package com.autos.autos_service.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas("autos_schema")
                .defaultSchema("autos_schema")
                .createSchemas(true)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}