package com.abdou.ebankingbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<String> handleBankAccountNotFoundException(BankAccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BankAccount not found");
        //code : code de status HTTP 404 (NOT FOUND)
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException (CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        //code de status HTTP 404 (NOT FOUND)
    }

    @ExceptionHandler(AccountBalanceNotSufficient.class)
    public ResponseEntity<String> handleAccountBalanceNotSufficient(AccountBalanceNotSufficient ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Balance Not Sufficient");
    }


}
