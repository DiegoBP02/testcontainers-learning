package com.programming.testcontainers.controllers;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabasePostgresqlContainer extends PostgreSQLContainer<DatabasePostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:latest";
    private static DatabasePostgresqlContainer container;

    private DatabasePostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static DatabasePostgresqlContainer getInstance() {
        if (container == null) {
            container = new DatabasePostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", "integration-tests-db");
        System.setProperty("DB_USERNAME", "sa");
        System.setProperty("DB_PASSWORD", "sa");
    }

    @Override
    public void stop() {
    }
}
