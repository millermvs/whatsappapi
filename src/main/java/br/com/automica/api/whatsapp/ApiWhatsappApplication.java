package br.com.automica.api.whatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApiWhatsappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiWhatsappApplication.class, args);
	}
}
