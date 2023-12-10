package com.abdou.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDto {
    private List<BankOperationDto> operationDto;
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;

}
