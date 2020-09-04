package co.edu.cedesistemas.payment.gateway.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class DummyCreditCardLoaderConfig {
    @Bean
    public Map<String, CreditCardConfig> creditCards() throws Exception {
        log.info("loading credit cards:");
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("credit-cards.json");
        List<CreditCardConfig> cards = mapper.readValue(is, new TypeReference<List>() {});
        log.info("credit cards available for testing:");
        log.info("**********************************************");
        cards.forEach(System.out::println);
        log.info("**********************************************");
        return cards.stream().collect(Collectors.toMap(CreditCardConfig::getNumber, c -> c));
    }
}