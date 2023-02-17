package com.banking.cqrs.core.handlers;

import com.banking.cqrs.core.domain.AggregateRoot;


public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregateRoot);
    T getById(String id);
}
