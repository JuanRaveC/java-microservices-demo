package co.edu.cedesistemas.payment.gateway.repository;

import co.edu.cedesistemas.payment.gateway.model.TransactionRes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionRes, String> {
    List<TransactionRes> findAllByState(TransactionRes.State state);
}
