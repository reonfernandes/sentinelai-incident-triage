package com.reon.sentinelcoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.reon.sentinelcoreservice",
        "com.reon.exception"
})
public class CoreService {

    public static void main(String[] args) {
        SpringApplication.run(CoreService.class, args);
    }

}
