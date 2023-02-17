package com.banking.account.cmd.infrastructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.exceptions.ConcurrencyException;
import com.banking.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {
    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggreteId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoreRepository.findByAggreteIdentifier(aggreteId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() -1 ).getVersion() != expectedVersion){
            throw new ConcurrencyException();
        }else {
            var version = expectedVersion;
            for (var event: events){
                version ++;
                event.setVersion(version);
                var eventModel = EventModel.builder()
                        .timeStamp(new Date())
                        .aggreteIdentifier(aggreteId)
                        .aggreteType(AccountAggregate.class.getTypeName())
                        .version(version)
                        .eventType(event.getClass().getTypeName())
                        .eventData(event)
                        .build();
                var persitedEvent =  eventStoreRepository.save(eventModel);
                if (persitedEvent != null){
                    //producir evento para kafka
                }
            }
        }
    }

    @Override
    public List<BaseEvent> getEvent(String aggregateId) {
       var eventStream = eventStoreRepository.findByAggreteIdentifier(aggregateId);
       if (eventStream == null || eventStream.isEmpty()){
           throw new AggregateNotFoundException("La cuenta de banco no es correcta");
       }else {
           return eventStream.stream().map(x -> x.getEventData()).collect(Collectors.toList());
       }
    }
}
