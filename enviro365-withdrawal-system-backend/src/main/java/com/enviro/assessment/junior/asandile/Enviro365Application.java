/**
 * Main Spring Boot Application Class for Enviro365 Withdrawal Management System
 *
 * @author Asandile
 * @version 1.0
 * @since 2024
 */
package com.enviro.assessment.junior.asandile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties
public class Enviro365Application {

    /**
     * Main entry point for the Spring Boot application
     * Loads environment variables from .env file before Spring Boot starts
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Enviro365Application.class);

        // Add environment variables from .env file
        app.addInitializers(applicationContext -> {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MutablePropertySources propertySources = environment.getPropertySources();

            try {
                Properties props = new Properties();
                // Load .env file from the root directory
                Files.lines(Paths.get(".env"))
                        .filter(line -> !line.trim().startsWith("#") && line.trim().contains("="))
                        .forEach(line -> {
                            String[] parts = line.split("=", 2);
                            if (parts.length == 2) {
                                props.setProperty(parts[0].trim(), parts[1].trim());
                                System.out.println("Loaded env: " + parts[0].trim() + "=" + parts[1].trim());
                            }
                        });

                Map<String, Object> envMap = new HashMap<>();
                props.forEach((key, value) -> envMap.put((String) key, value));
                propertySources.addFirst(new MapPropertySource("dotenv-properties", envMap));

                System.out.println("Environment variables loaded from .env file successfully");
            } catch (IOException e) {
                System.out.println("No .env file found at: " + Paths.get(".env").toAbsolutePath());
                System.out.println("Please create a .env file with required configuration");
                System.out.println("Using system environment variables or defaults");
            }
        });

        app.run(args);
    }
}