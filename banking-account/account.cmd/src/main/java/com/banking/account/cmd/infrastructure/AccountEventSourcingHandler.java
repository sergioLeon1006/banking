package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.domain.AggregateRoot;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import com.banking.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;

public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
   @Autowired
   private EventStore eventStore;
    @Override
    public void save(AggregateRoot aggregateRoot) {
        eventStore.saveEvents(aggregateRoot.getId(),aggregateRoot.getUncommitesChanges(),aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommites();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvent(aggregate.getId());
        if (events != null && !events.isEmpty()){
            aggregate.replayEvents(events);
            var lastestVersion = events.stream().map(x-> x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(lastestVersion.get());
        }
        return aggregate;
    }
}
