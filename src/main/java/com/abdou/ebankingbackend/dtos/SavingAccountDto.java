package com.abdou.ebankingbackend.dtos;

import lombok.Data;

@Data
public class SavingAccountDto extends BankAccountDto{
    private double interestRate;
}
