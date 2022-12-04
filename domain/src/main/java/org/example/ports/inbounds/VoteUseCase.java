package org.example.ports.inbounds;

import org.example.domain.model.Vote;
import org.example.domain.model.VoteResult;

public interface VoteUseCase {

    VoteResult sendVote(Vote vote);

}
