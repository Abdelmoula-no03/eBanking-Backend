package com.abdou.ebankingbackend.services;

import com.abdou.ebankingbackend.dtos.*;
import com.abdou.ebankingbackend.entites.*;
import com.abdou.ebankingbackend.enums.AccountStatus;
import com.abdou.ebankingbackend.enums.OperationType;
import com.abdou.ebankingbackend.exceptions.AccountBalanceNotSufficient;
import com.abdou.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.abdou.ebankingbackend.exceptions.CustomerNotFoundException;
import com.abdou.ebankingbackend.mappers.BankAccountMapper;
import com.abdou.ebankingbackend.repositories.BankAccountRepository;
import com.abdou.ebankingbackend.repositories.BankOperationRepository;
import com.abdou.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private BankAccountRepository bankAccountRepository;
    private BankOperationRepository bankOperationRepository;
    private CustomerRepository customerRepository;
    private BankAccountMapper bankAccountMapper;
    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        log.info("Saving new customer !!");
        Customer customer = bankAccountMapper.fromCustomerDto(customerDto);
        customerRepository.save(customer);
        return customerDto;
    }
    @Override
    public CurrentAccountDto saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException{
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setCustomer(customer);
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(currentAccount);
    }

    @Override
    public SavingAccountDto saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setCustomer(customer);
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        bankAccountRepository.save(savingAccount);
        return bankAccountMapper.fromSavingAccount(savingAccount);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(bankAccountMapper::fromCustomer)
                .toList();
    }

    @Override
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {
     BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Account noy found !!"));
     if (bankAccount instanceof SavingAccount){
         return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
     }
     return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, AccountBalanceNotSufficient {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Account noy found !!"));
        if (bankAccount.getBalance()< amount)
            throw new AccountBalanceNotSufficient("Account balance Not sufficient");
        BankOperation bankOperation = new BankOperation();
        bankOperation.setType(OperationType.DEBIT);
        bankOperation.setAmount(amount);
        bankOperation.setOperationDate(new Date());
        bankOperation.setDescription(description);
        bankOperation.setBankAccount(bankAccount);
        bankOperationRepository.save(bankOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Account noy found !!"));
        BankOperation bankOperation = new BankOperation();
        bankOperation.setType(OperationType.CREDIT);
        bankOperation.setAmount(amount);
        bankOperation.setOperationDate(new Date());
        bankOperation.setDescription(description);
        bankOperation.setBankAccount(bankAccount);
        bankOperationRepository.save(bankOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountDestinationId, String accountSourceId, double amount) throws BankAccountNotFoundException, AccountBalanceNotSufficient {
        debit(accountSourceId, amount, "Transfer to"+accountDestinationId);
        credit(accountDestinationId, amount, "Transfer from" + accountSourceId);
    }

    @Override
    public List<BankAccountDto> bankAccountList() {
        return bankAccountRepository.findAll()
                .stream()
                .map(bankAccount -> {
                if ( bankAccount instanceof CurrentAccount){
                    return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
                } else {
                    return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
                }
        }).toList();

    }

    @Override
    public CustomerDto getCustomer (Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found !!"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Customer updated");
        Customer customer = bankAccountMapper.fromCustomerDto(customerDto);
        customerRepository.save(customer);
        return customerDto;
    }

    @Override
    public void deleteCustomer(Long customerId){

        customerRepository.deleteById(customerId);
    }

    @Override
    public List<BankOperationDto> getAccountHistory(String accountId){
        return bankOperationRepository.findByBankAccountId(accountId)
                .stream()
                .map(bankAccountMapper::fromBankOperation)
                .toList();
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->
                        new BankAccountNotFoundException("account not found !!"));
        Page<BankOperation> history = bankOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        List<BankOperationDto> operationDtos = history
                .getContent()
                .stream()
                .map(op -> bankAccountMapper.fromBankOperation(op))
                .toList();
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        accountHistoryDto.setOperationDto(operationDtos);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setAccountId(accountId);
        accountHistoryDto.setTotalPages(history.getTotalPages());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setBalance(bankAccount.getBalance());
        return accountHistoryDto;
    }

    @Override
    public List<CustomerDto> searchCustomerss(String keyword) {

        return this.customerRepository.
                findByNameContains(keyword)
                .stream()
                .map(bankAccountMapper::fromCustomer)
                .toList();
    }


}
