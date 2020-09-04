package co.edu.cedesistemas.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("transaction")
public class TransactionRes {
    @Id
    private String id;
    private String orderId;
    private State state;
    private String paymentNetworkResponseCode;
    private String paymentNetworkResponseErrorMessage;
    private String traceabilityCode;
    private String authorizationCode;
    private String pendingReason;
    private String responseCode;
    private String errorCode;
    private String responseMessage;
    private LocalDateTime transactionDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CreditCard card;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String notifyUrl;
    private Float value;

    public enum  State {
        APPROVED,
        DECLINED,
        PENDING
    }
}
