package com.abdou.ebankingbackend.dtos;

import com.abdou.ebankingbackend.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class BankOperationDto {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;

}
