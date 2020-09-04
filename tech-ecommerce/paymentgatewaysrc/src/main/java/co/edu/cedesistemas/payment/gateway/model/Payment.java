package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

@Data
public class Payment {
    private Merchant merchant;
    private TransactionReq transaction;
}
