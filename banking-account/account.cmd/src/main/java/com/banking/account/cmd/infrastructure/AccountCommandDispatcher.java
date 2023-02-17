package com.banking.account.cmd.infrastructure;

import com.banking.cqrs.core.comands.BaseCommand;
import com.banking.cqrs.core.comands.CommandHandlerMethod;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();
    @Override
    public <T extends BaseCommand> void registrerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());
        if (handlers == null || handlers.size() == 0){
            throw new RuntimeException("El command handler no fué registrado");
        } else if (handlers.size()>1) {
            throw new RuntimeException("No se puede enviar un command que tiene mas de un handler");
        }else{
            handlers.get(0).handle(command);
        }
    }
}
