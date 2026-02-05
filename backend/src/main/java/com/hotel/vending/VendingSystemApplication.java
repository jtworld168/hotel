package com.hotel.vending;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hotel Vending System Application
 */
@SpringBootApplication
@MapperScan("com.hotel.vending.mapper")
public class VendingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendingSystemApplication.class, args);
    }
}
