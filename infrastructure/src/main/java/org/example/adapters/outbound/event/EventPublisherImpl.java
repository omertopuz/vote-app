package org.example.adapters.outbound.event;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.model.UserNotification;
import org.example.domain.model.VoteCastEventModel;
import org.example.ports.outbounds.EventPublisher;

@Slf4j
public class EventPublisherImpl implements EventPublisher {
    @Override
    public void voteCastedEvent(VoteCastEventModel voteCastEventModel) {
        log.info("User completed voting", voteCastEventModel);
    }

    @Override
    public void notifyUser(UserNotification userNotification) {
        log.info("User notified after voting completed", userNotification);
    }
}
