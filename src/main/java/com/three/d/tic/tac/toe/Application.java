package com.three.d.tic.tac.toe;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT60S" /* 60 seconds */)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
