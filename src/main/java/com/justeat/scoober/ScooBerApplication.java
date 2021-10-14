package com.justeat.scoober;

import com.justeat.scoober.service.ScooberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@SpringBootApplication
@Slf4j
@Profile("!test")
public class ScooBerApplication implements CommandLineRunner {
    private ScooberService scooberService;
    public static final String SERVER_PORT = System.getProperty("server.port");

    @Autowired
    public ScooBerApplication(ScooberService scooberService) {
        this.scooberService = scooberService;
    }

    public static void main(String[] args) {
        log.info("\nProperties set, bringing up the application");
        SpringApplication app = new SpringApplication(ScooBerApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", SERVER_PORT));
        app.run(args);
    }

    @Override
    public void run(String... args) {
        scooberService.startGame();
    }

}
