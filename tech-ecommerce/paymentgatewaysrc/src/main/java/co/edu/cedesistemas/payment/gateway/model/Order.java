package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

@Data
public class Order {
    private String id;
    private String accountId;
    private String referenceCode;
    private String description;
    private String signature;
    private String notifyUrl;
    private Float value;
    private Person buyer;
    private Address shippingAddress;
}
