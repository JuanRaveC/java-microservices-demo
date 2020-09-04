package co.edu.cedesistemas.payment.gateway.model;

import lombok.Data;

@Data
public class Address {
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
}
