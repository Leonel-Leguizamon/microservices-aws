package com.dlsoftware.msvc.items_msvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableConfigurationProperties
@SpringBootApplication
public class ItemsMsvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemsMsvcApplication.class, args);
	}

}
