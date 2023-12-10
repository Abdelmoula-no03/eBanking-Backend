package com.abdou.ebankingbackend.repositories;

import com.abdou.ebankingbackend.entites.BankOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankOperationRepository extends JpaRepository<BankOperation,Long> {

    List<BankOperation> findByBankAccountId(String accountId);
    Page<BankOperation> findByBankAccountId(String accountId, Pageable pageable);
}
