package com.banking.cqrs.core.infrastructure;

import com.banking.cqrs.core.comands.BaseCommand;
import com.banking.cqrs.core.comands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void  registrerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
