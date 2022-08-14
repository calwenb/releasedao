package com.boy.softwarecrm;

import com.boy.softwarecrm.utils.LoggerUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoftwarecrmApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoftwarecrmApplication.class, args);
        LoggerUtil.info("start ok ~", SoftwarecrmApplication.class);
    }

}
