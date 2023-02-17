package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountCloseEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FoundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build()
        );
    }
    public void apply(AccountOpenedEvent event){
        this.id = event.getId();
        this.active= true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFounds(double amount){
        if (!this.active){
            throw  new IllegalStateException("Los fondos no pueden ser ingresados a esta cuenta");
        } else if (amount <= 0) {
            throw new IllegalStateException("El deposito de dinero no puede ser menor o igual a 0");
        }else {
            raiseEvent(FoundsDepositedEvent.builder()
                    .id(this.id)
                    .amount(amount)
                    .build()
            );
        }
    }
    public void apply(FoundsDepositedEvent event){
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("La cuenta está cerrada");
        }else {
            raiseEvent(FundsWithdrawEvent.builder()
                    .id(this.id)
                    .amount(amount)
                    .build()
            );
        }
    }
    public void apply(FundsWithdrawEvent event){
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount(){
        if (!this.active){
            throw new IllegalStateException("La cuenta ya se encuentra cerrada");
        }else {
            raiseEvent(AccountCloseEvent.builder()
                    .id(this.id)
                    .build()
            );
        }
    }
    public void apply(AccountCloseEvent event){
        this.id = event.getId();
        this.active = false;
    }

    public double getBalance(){
        return this.balance;
    }
}
