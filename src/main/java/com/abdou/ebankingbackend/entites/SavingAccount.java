package com.abdou.ebankingbackend.entites;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@DiscriminatorValue("SAVE")
public class SavingAccount extends BankAccount{

    private double interestRate;
}
