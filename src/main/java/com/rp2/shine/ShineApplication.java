package com.rp2.shine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 짝궁
@EnableJpaAuditing
@SpringBootApplication
public class ShineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShineApplication.class, args);
    }

}
