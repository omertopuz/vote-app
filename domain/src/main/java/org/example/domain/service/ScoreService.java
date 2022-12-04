package org.example.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.exception.ElectionServiceException;
import org.example.domain.exception.EntityNotFoundException;
import org.example.domain.model.Score;
import org.example.domain.model.ScoreRequest;
import org.example.domain.model.VoteCastEventModel;
import org.example.ports.inbounds.EventListen;
import org.example.ports.inbounds.ScoreUseCase;
import org.example.ports.outbounds.ExternalServiceAccess;
import org.example.ports.outbounds.VotePersistence;

@RequiredArgsConstructor
public class ScoreService implements ScoreUseCase, EventListen {

    private final ExternalServiceAccess externalServiceAccess;
    private final VotePersistence votePersistence;

    @Override
    public Score showScore(ScoreRequest scoreRequest) {
        try {
            externalServiceAccess.getElectionById(scoreRequest.getElectionId());
            return votePersistence.getScore(scoreRequest);
        }catch (ElectionServiceException exception){
            //assuming election api returns 404 for that request and the exception code defines user not found.
            throw new EntityNotFoundException("Election id invalid for " + scoreRequest.getElectionId());
        }
    }

    @Override
    public void voteCastedEventListen(VoteCastEventModel voteCastEventModel) {
        // increment candidate vote
        votePersistence.updateScore(voteCastEventModel.getElectionId(), voteCastEventModel.getCandidateId());
    }
}
