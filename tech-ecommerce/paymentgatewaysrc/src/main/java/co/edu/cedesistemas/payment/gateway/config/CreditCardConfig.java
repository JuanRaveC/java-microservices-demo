package co.edu.cedesistemas.payment.gateway.config;

import lombok.Data;

@Data
public class CreditCardConfig {
    private String number;
    private Integer cvc;
    private Integer expMonth;
    private Integer expYear;
    private Status status;
    private Float limit;
    private Float credit;

    public enum Status {
        VALID,
        INVALID
    }

    @Override
    public String toString() {
        return "credit-card {number: " + number + ", month: " + expMonth + ", year: " + expYear + "," +
                "cvc: " + cvc + ", status: " + status + ", credit: " + credit + "}";
    }
}
