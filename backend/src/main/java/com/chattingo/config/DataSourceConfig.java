package com.chattingo.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();

        String url = dotenv.get("SPRING_DATASOURCE_URL", "jdbc:mysql://localhost:3306/chattingo_db?createDatabaseIfNotExist=true");
        String username = dotenv.get("SPRING_DATASOURCE_USERNAME", "admin");
        String password = dotenv.get("SPRING_DATASOURCE_PASSWORD", "admin123");

        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
