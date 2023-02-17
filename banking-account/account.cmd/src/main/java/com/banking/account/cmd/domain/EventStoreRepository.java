package com.banking.account.cmd.domain;

import com.banking.cqrs.core.events.EventModel;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventStoreRepository extends MongoRepository<EventModel,String> {
   List<EventModel> findByAggreteIdentifier(String aggreteIdentifier);
}
