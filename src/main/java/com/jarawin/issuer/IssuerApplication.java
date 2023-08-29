package com.jarawin.issuer;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// import com.jarawin.issuer.entity.Acquirer;
// import com.jarawin.issuer.repository.AcquirerRepository;

// import com.jarawin.issuer.entity.Card;
// import com.jarawin.issuer.repository.CardRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@EnableWebMvc
@SpringBootApplication
@EnableJpaRepositories
@CrossOrigin(origins = "*")
public class IssuerApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder().build();
	}

	// @Autowired
	// private AcquirerRepository acquirerRepository;

	// @Autowired
	// private CardRepository cardRepository;

	@PostConstruct
	public void initialData() {
		// Acquirer acquirer1 = Acquirer.builder()
		// .Country("Thailand")
		// .Name("Kasikorn Bank")
		// .Category("Large")
		// .Status("pending")
		// .build();
		// acquirerRepository.save(acquirer1);
		// System.out.println("Acquirer 1: " + acquirer1.toString());

		// Card card1 = new Card(
		// "",
		// "Visa",
		// "John Doe",
		// "pending",
		// "200000",
		// "10000");
		// cardRepository.save(card1);
	}

	public static void main(String[] args) {
		// ConfigurableApplicationContext context =
		SpringApplication.run(IssuerApplication.class, args);
	}

}
