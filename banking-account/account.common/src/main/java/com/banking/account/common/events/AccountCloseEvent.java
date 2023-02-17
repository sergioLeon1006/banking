package com.banking.account.common.events;

import com.banking.cqrs.core.events.BaseEvent;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AccountCloseEvent extends BaseEvent {
}
