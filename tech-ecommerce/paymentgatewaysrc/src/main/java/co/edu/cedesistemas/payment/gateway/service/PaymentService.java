package co.edu.cedesistemas.payment.gateway.service;

import co.edu.cedesistemas.payment.gateway.client.RestClient;
import co.edu.cedesistemas.payment.gateway.config.CreditCardConfig;
import co.edu.cedesistemas.payment.gateway.model.CreditCard;
import co.edu.cedesistemas.payment.gateway.model.Order;
import co.edu.cedesistemas.payment.gateway.model.TransactionReq;
import co.edu.cedesistemas.payment.gateway.model.TransactionRes;
import co.edu.cedesistemas.payment.gateway.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private final Map<String, CreditCardConfig> cards;
    private final TransactionRepository repository;
    private final RestClient restClient;

    public TransactionRes pay(TransactionReq tx) {
        CreditCard card = tx.getCreditCard();
        Order order = tx.getOrder();

        Float value = order.getValue();

        TransactionRes res = new TransactionRes();
        res.setId(UUID.randomUUID().toString());
        res.setOrderId(tx.getOrder().getId());
        res.setTransactionDate(LocalDateTime.now());
        res.setResponseMessage("transaction pending");
        res.setResponseCode("2");
        res.setTraceabilityCode(RandomStringUtils.randomAlphabetic(8));
        res.setState(TransactionRes.State.PENDING);
        res.setCard(card);
        res.setValue(value);
        res.setNotifyUrl(order.getNotifyUrl());

        return repository.save(res);
    }

    @Scheduled(fixedRate = 20000)
    private void processPayment() {
        List<TransactionRes> trxs = repository.findAllByState(TransactionRes.State.PENDING);
        log.info("processing pending payments: {}", trxs.size());
        trxs.forEach(this::handleTransaction);
    }

    private void handleTransaction(final TransactionRes t) {
        CreditCard card = t.getCard();
        Float value = t.getValue();
        try {
            pay(card, value);
            t.setAuthorizationCode(RandomStringUtils.randomNumeric(5));
            t.setResponseMessage("success");
            t.setResponseCode("0");
            t.setState(TransactionRes.State.APPROVED);

        } catch (Exception ex) {
            t.setResponseMessage(ex.getMessage());
            t.setResponseCode("1");
            t.setState(TransactionRes.State.DECLINED);
            t.setErrorCode("" + ex.getMessage().hashCode());
        }
        repository.save(t);
        log.info("calling back confirmation to merchant");
        restClient.callback(t);
    }

    private void pay(CreditCard card, Float value) throws Exception {
        log.info("performing payment card: {}", card.getNumber());
        validateCard(card);
        validateFunds(card, value);
        CreditCardConfig cardConfig = cards.get(card.getNumber());
        log.info("current credit for card {}: {}", cardConfig.getNumber(), cardConfig.getCredit());
        cardConfig.setCredit(cardConfig.getCredit() - value);
        log.info("new credit for card {}: {}", cardConfig.getNumber(), cardConfig.getCredit());
    }

    private void validateCard(CreditCard card) throws Exception {
        String number = card.getNumber();
        CreditCardConfig cardConfig = cards.get(number);
        if (cardConfig == null) {
            throw new Exception("card not found");
        }

        if (cardConfig.getStatus().equals(CreditCardConfig.Status.INVALID)) {
            throw new Exception("invalid card");
        }

        Integer cvc = card.getSecurityCode();
        if (!cvc.equals(cardConfig.getCvc())) {
            throw new Exception("invalid security code");
        }

        Integer expMonth = card.getExpirationMonth();
        if (!expMonth.equals(cardConfig.getExpMonth())) {
            throw new Exception("invalid expiration date");
        }

        Integer expYear = card.getExpirationYear();
        if (!expYear.equals(cardConfig.getExpYear())) {
            throw new Exception("invalid expiration date");
        }
    }

    private void validateFunds(CreditCard card, Float value) throws Exception {
        CreditCardConfig cardConfig = cards.get(card.getNumber());

        Float credit = cardConfig.getCredit();
        if (credit - value < 0.0) {
            throw new Exception("no funds");
        }
    }
}