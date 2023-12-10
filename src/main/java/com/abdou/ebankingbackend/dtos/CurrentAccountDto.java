package com.abdou.ebankingbackend.dtos;

import lombok.Data;

@Data
public class CurrentAccountDto extends BankAccountDto{
    private double overDraft;
}
