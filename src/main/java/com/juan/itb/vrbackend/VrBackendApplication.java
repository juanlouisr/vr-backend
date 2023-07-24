package com.juan.itb.vrbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VrBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VrBackendApplication.class, args);
	}

//	@Bean
//	ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
//		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//		initializer.setConnectionFactory(connectionFactory);
//		ResourceDatabasePopulator resource =
//				new ResourceDatabasePopulator(new ClassPathResource("schema.mysql"));
//		initializer.setDatabasePopulator(resource);
//		return initializer;
//	}

}
