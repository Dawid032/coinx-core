package com.coinx.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoinxCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinxCoreApplication.class, args);
    }

}
