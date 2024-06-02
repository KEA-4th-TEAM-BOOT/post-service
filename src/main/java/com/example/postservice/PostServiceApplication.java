package com.example.postservice;

//import com.example.postservice.config.DatasourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
//@EnableConfigurationProperties(DatasourceProperties.class)
public class PostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostServiceApplication.class, args);
    }

}
