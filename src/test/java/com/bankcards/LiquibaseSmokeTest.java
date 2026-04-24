package com.bankcards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(classes = BankCardsApplication.class)
public class LiquibaseSmokeTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("bankcards")
            .withUsername("bankcards")
            .withPassword("bankcards");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.change-log",
                () -> "classpath:db/changelog/db.changelog-master.yaml");
    }

    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    void schemaExistsAndWeCanQuery() {
        Long n = jdbcTemplate.queryForObject("select count(*) from cards", Long.class);
        assertNotNull(n);
    }
}
