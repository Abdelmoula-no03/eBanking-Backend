package com.abdou.ebankingbackend.repositories;

import com.abdou.ebankingbackend.entites.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
