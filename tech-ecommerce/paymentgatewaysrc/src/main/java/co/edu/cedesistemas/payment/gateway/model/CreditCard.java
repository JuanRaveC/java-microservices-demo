package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

@Data
public class CreditCard {
    private String number;
    private Integer securityCode;
    private Integer expirationMonth;
    private Integer expirationYear;
    private String name;
}