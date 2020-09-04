package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

@Data
public class Person {
    private String merchantId;
    private String fullName;
    private String email;
    private String phone;
    private String dni;
}