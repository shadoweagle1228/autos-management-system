package com.autos.user_auth_service.infrastructure.config;

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
                .schemas("users_schema")
                .defaultSchema("users_schema")
                .createSchemas(true) // Esto crea el esquema si no existe
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}