package com.abdou.ebankingbackend;

import com.abdou.ebankingbackend.entites.*;
import com.abdou.ebankingbackend.enums.AccountStatus;
import com.abdou.ebankingbackend.enums.OperationType;
import com.abdou.ebankingbackend.repositories.BankAccountRepository;
import com.abdou.ebankingbackend.repositories.BankOperationRepository;
import com.abdou.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository,
                            BankOperationRepository bankOperationRepository,
                            CustomerRepository customerRepository
                            ) {
        return args -> {
            Stream.of("Samir", "Marwan", "Ilyass", "Mohammed")
                    .forEach(name ->
                    {
                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setEmail(name + "@gmail.com");
                        customerRepository.save(customer);

                    });
            customerRepository.findAll().forEach(
                    cust -> {
                        CurrentAccount currentAccount = new CurrentAccount();
                        currentAccount.setOverDraft(1000);
                        currentAccount.setId(UUID.randomUUID().toString());
                        currentAccount.setBalance(8000*Math.random());
                        currentAccount.setCreatedAt(new Date());
                        currentAccount.setStatus(AccountStatus.CREATED);
                        currentAccount.setCustomer(cust);
                        bankAccountRepository.save(currentAccount);


                        SavingAccount savingAccount = new SavingAccount();
                        savingAccount.setInterestRate(6*Math.random());
                        savingAccount.setId(UUID.randomUUID().toString());
                        savingAccount.setBalance(8000*Math.random());
                        savingAccount.setCreatedAt(new Date());
                        savingAccount.setStatus(AccountStatus.CREATED);
                        savingAccount.setCustomer(cust);
                        bankAccountRepository.save(savingAccount);


                    }
            );
            bankAccountRepository.findAll().forEach(
                    acc -> {
                        for (int i = 0;i < 6 ;i++) {
                            BankOperation bankOperation = new BankOperation();
                            bankOperation.setOperationDate(new Date());
                            bankOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                            bankOperation.setAmount(Math.random()*6000);
                            bankOperation.setBankAccount(acc);
                            bankOperationRepository.save(bankOperation);
                        }
                    }
            );

        };
    }

}
