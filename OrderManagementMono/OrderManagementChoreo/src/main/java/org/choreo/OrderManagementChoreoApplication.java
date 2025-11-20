package org.choreo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderManagementChoreoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementChoreoApplication.class, args);
	}

}
