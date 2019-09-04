package com.brain.passwd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class PasswdApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasswdApplication.class, args);
    }
}
