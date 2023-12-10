package com.abdou.ebankingbackend.mappers;

import com.abdou.ebankingbackend.dtos.BankOperationDto;
import com.abdou.ebankingbackend.dtos.CurrentAccountDto;
import com.abdou.ebankingbackend.dtos.CustomerDto;
import com.abdou.ebankingbackend.dtos.SavingAccountDto;
import com.abdou.ebankingbackend.entites.BankOperation;
import com.abdou.ebankingbackend.entites.CurrentAccount;
import com.abdou.ebankingbackend.entites.Customer;
import com.abdou.ebankingbackend.entites.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {
    public CustomerDto fromCustomer (Customer customer){
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);
        return customerDto;
    }

    public Customer fromCustomerDto (CustomerDto customerDto){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);
        return customer;
    }

    public CurrentAccountDto fromCurrentAccount(CurrentAccount currentAccount){
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        BeanUtils.copyProperties(currentAccount,currentAccountDto);
        currentAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentAccountDto.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDto;
    }

    public SavingAccountDto fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        BeanUtils.copyProperties(savingAccount,savingAccountDto);
        savingAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingAccountDto.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDto;
    }

    public CurrentAccount fromCurrentAccountDto(CurrentAccountDto currentAccountDto){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDto, currentAccount);
        currentAccount.setCustomer(fromCustomerDto(currentAccountDto.getCustomerDto()));
        return currentAccount;
    }

    public SavingAccount fromSavingAccountDto(SavingAccountDto savingAccountDto){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDto, savingAccount);
        savingAccount.setCustomer(fromCustomerDto(savingAccountDto.getCustomerDto()));
        return savingAccount;
    }

    public BankOperationDto fromBankOperation (BankOperation bankOperation){
        BankOperationDto bankOperationDto = new BankOperationDto();
        BeanUtils.copyProperties(bankOperation,bankOperationDto);
        return bankOperationDto;
    }

    public BankOperation fromBankOperationDto (BankOperationDto bankOperationDto){
        BankOperation bankOperation = new BankOperation();
        BeanUtils.copyProperties(bankOperationDto,bankOperation);
        return bankOperation;
    }



}
