package com.dlsoftware.msvc.products_msvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Se utiliza para que lo reconozca como entidad JPA
@EntityScan({"com.dlsoftware.msvc.libs.commons.entities"})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties
@SpringBootApplication
public class ProductsMsvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsMsvcApplication.class, args);
	}

}
