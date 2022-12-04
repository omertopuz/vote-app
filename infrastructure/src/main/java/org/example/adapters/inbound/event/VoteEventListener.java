package org.example.adapters.inbound.event;

import lombok.RequiredArgsConstructor;
import org.example.domain.model.VoteCastEventModel;
import org.example.ports.inbounds.EventListen;

@RequiredArgsConstructor
public class VoteEventListener {

    /*
    * This is the class to import the listener toll such as Kafka, Rabbit etc.
    * */
    private final EventListen voteEventListen;

    public void voteCastedEventListen(VoteCastEventModel voteCastEventModel) {
        voteEventListen.voteCastedEventListen(voteCastEventModel);
    }
}
