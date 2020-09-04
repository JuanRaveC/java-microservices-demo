package co.edu.cedesistemas.payment.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentGatewayApp {
	public static void main(String[] args) {
		SpringApplication.run(PaymentGatewayApp.class, args);
	}
}
