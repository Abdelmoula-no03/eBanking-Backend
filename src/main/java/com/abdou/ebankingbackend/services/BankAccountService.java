package com.abdou.ebankingbackend.services;

import com.abdou.ebankingbackend.dtos.*;
import com.abdou.ebankingbackend.exceptions.AccountBalanceNotSufficient;
import com.abdou.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.abdou.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDto saveCustomer (CustomerDto customerDto);
    CurrentAccountDto saveCurrentAccount (double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDto saveSavingAccount (double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDto> listCustomers();
    BankAccountDto getBankAccount (String accountId) throws BankAccountNotFoundException;
    void debit (String accountId, double amount, String description) throws BankAccountNotFoundException, AccountBalanceNotSufficient;
    void credit (String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer (String accountDestinationId, String accountSourceId, double amount) throws BankAccountNotFoundException, AccountBalanceNotSufficient;
    List<BankAccountDto> bankAccountList();


    CustomerDto getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDto updateCustomer(CustomerDto customerDto);

    void deleteCustomer(Long customerId);

    List<BankOperationDto> getAccountHistory(String accountId);
    AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;


    List<CustomerDto> searchCustomerss(String keyword);
}
