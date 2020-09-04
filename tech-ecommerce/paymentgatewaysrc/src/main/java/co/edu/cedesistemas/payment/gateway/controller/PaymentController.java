package co.edu.cedesistemas.payment.gateway.controller;

import co.edu.cedesistemas.payment.gateway.model.TransactionReq;
import co.edu.cedesistemas.payment.gateway.model.TransactionRes;
import co.edu.cedesistemas.payment.gateway.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/payments")
    public ResponseEntity<TransactionRes> pay(@RequestBody TransactionReq req) {
        TransactionRes res = service.pay(req);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
