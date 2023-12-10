package com.abdou.ebankingbackend.web;

import com.abdou.ebankingbackend.dtos.*;
import com.abdou.ebankingbackend.exceptions.AccountBalanceNotSufficient;
import com.abdou.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.abdou.ebankingbackend.exceptions.CustomerNotFoundException;
import com.abdou.ebankingbackend.services.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private final BankAccountService bankAccountService;
    BankAccountRestAPI(BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDto getAccount (@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDto> getAccountsList (){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public AccountHistoryDto getOperations (@PathVariable String accountId,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/accounts/currentAccount")
    public CurrentAccountDto saveCurrentAccount(@RequestParam(name = "initialBalance", defaultValue = "0.0") double initialBalance,
                                                @RequestParam(name = "overDraft") double overDraft,
                                                @RequestParam(name = "customerId") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentAccount(initialBalance,overDraft,customerId);
    }

    @PostMapping("/accounts/savingAccount")
    public SavingAccountDto saveSavingAccount(@RequestParam(name = "initialBalance", defaultValue = "0.0") double initialBalance,
                                                          @RequestParam(name = "interestRate") double interestRate,
                                                          @RequestParam(name = "customerId") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveSavingAccount(initialBalance,interestRate,customerId);
    }
    @PostMapping("accounts/credit")
    @ResponseStatus(HttpStatus.OK)
    public void creditOperation (@RequestParam(name = "accountId") String accountId,
                        @RequestParam(name = "amount") double amount,
                        @RequestParam(name = "description") String description) throws BankAccountNotFoundException {
        bankAccountService.credit(accountId,amount,description);
    }

    @PostMapping("accounts/debit")
    @ResponseStatus(HttpStatus.OK)
    public void debitOperation (@RequestParam(name = "accountId") String accountId,
                        @RequestParam(name = "amount") double amount,
                        @RequestParam(name = "description") String description) throws BankAccountNotFoundException, AccountBalanceNotSufficient {
        bankAccountService.debit(accountId,amount,description);
    }

    @PostMapping("accounts/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferOperation (@RequestParam(name = "accountDestinationId") String accountDestinationId,
                        @RequestParam(name = "accountSourceId") String accountSourceId,
                        @RequestParam(name = "amount") double amount) throws BankAccountNotFoundException, AccountBalanceNotSufficient {
        bankAccountService.transfer(accountDestinationId,accountSourceId,amount);
    }







}
