package com.abdou.ebankingbackend.dtos;

import com.abdou.ebankingbackend.enums.AccountStatus;
import lombok.Data;


import java.util.Date;

@Data
public class BankAccountDto {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDto customerDto;
    private String type;
}
