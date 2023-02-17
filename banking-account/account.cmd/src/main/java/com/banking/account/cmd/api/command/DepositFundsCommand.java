package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.comands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
