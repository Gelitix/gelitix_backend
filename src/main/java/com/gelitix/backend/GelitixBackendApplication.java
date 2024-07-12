package com.gelitix.backend;

import com.gelitix.backend.config.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@EnableScheduling
public class GelitixBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GelitixBackendApplication.class, args);
    }

}
