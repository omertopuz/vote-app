package org.example.ports.inbounds;

import org.example.domain.model.VoteCastEventModel;

public interface EventListen {

    void voteCastedEventListen(VoteCastEventModel voteCastEventModel);
    
}
