package co.edu.cedesistemas.payment.gateway.client;

import co.edu.cedesistemas.payment.gateway.model.TransactionRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class RestClient {
    private final RestTemplate template;

    public void callback(TransactionRes res) {
        HttpEntity<TransactionRes> entity = new HttpEntity<>(res);
        ResponseEntity<String> response = template.postForEntity(res.getNotifyUrl(), entity, String.class);
        log.info(response.getBody());
    }
}
