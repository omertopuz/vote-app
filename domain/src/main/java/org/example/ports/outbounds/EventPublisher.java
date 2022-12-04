package org.example.ports.outbounds;

import org.example.domain.model.UserNotification;
import org.example.domain.model.VoteCastEventModel;

public interface EventPublisher {
    void voteCastedEvent(VoteCastEventModel voteCastEventModel);
    void notifyUser(UserNotification userNotification);

}
