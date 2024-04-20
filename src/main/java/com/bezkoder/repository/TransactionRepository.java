package com.bezkoder.repository;

import com.bezkoder.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
     List<Transaction> findAllBySender(String sender);
     List<Transaction> findAllByReceiver(String receiver);
}
