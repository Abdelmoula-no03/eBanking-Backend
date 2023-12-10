package com.abdou.ebankingbackend.exceptions;

public class AccountBalanceNotSufficient extends Exception{
    public AccountBalanceNotSufficient(String s){
        super(s);
    }
}
