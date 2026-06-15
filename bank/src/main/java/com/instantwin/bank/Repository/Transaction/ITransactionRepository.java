package com.instantwin.bank.Repository.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instantwin.bank.Model.Transaction.TransactionEntity;

@Repository
public interface ITransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByUserId(Long userId);
}
