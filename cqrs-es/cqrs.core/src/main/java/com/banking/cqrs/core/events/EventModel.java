package com.banking.cqrs.core.events;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collation = "EventStore")
public class EventModel {
    @Id
    private String id;
    private Date timeStamp;
    private String aggreteIdentifier;
    private String aggreteType;
    private int version;
    private String eventType;
    private BaseEvent eventData;
}
