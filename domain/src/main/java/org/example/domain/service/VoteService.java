package org.example.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.exception.*;
import org.example.domain.model.*;
import org.example.ports.inbounds.VoteUseCase;
import org.example.ports.outbounds.EventPublisher;
import org.example.ports.outbounds.ExternalServiceAccess;
import org.example.ports.outbounds.VotePersistence;

import java.util.Date;

@RequiredArgsConstructor
public class VoteService implements VoteUseCase {
    private final VotePersistence votePersistence;
    private final ExternalServiceAccess externalServiceAccess;
    private final EventPublisher eventPublisher;

    @Override
    public VoteResult sendVote(Vote vote) {
        Election election = getElection(vote.getElectionId());
        checkElectionIsOver(election);
        UserInfo userInfo = getUserInfo(vote.getUserId());
        checkUserSecurityItemValid(vote.getUserId(), vote.getSecurityItem());
        UserVote userVote = new UserVote(vote.getUserId(), vote.getElectionId(), null);
        checkUserVotedAlready(userVote);

        votePersistence.saveUserVote(userVote);
        eventPublisher.voteCastedEvent(new VoteCastEventModel(vote.getElectionId(), vote.getCandidateId()));
        votePersistence.updateScore(vote.getElectionId(), vote.getCandidateId());

        eventPublisher.notifyUser(new UserNotification(userInfo.getUserContactInfo(), "Voting", "Voting completed"));
        return new VoteResult(true, "completed");
    }

    private Election getElection(Integer electionId) {
        try {
            return externalServiceAccess.getElectionById(electionId);
        } catch (ElectionServiceException exception) {
            //assuming election api returns 404 for that request and the exception code defines user not found.
            throw new EntityNotFoundException("Election id invalid for " + electionId);
        }
    }

    private void checkElectionIsOver(Election election) {
        Date currentTime = new Date();
        if (currentTime.after(election.getEndDate()))
            throw new VoteDomainException("Voting process is over");
    }

    private void checkUserSecurityItemValid(String userId, String securityItem) {
        UserSecurityItem userSecurityItem = externalServiceAccess
                .checkUserWithSecurityItem(userId, securityItem);
        if (!userSecurityItem.isValid())
            throw new SecurityItemInvalidException();

    }

    private void checkUserVotedAlready(UserVote userVote) {
        votePersistence.getUserVote(userVote)
                .ifPresent(uv -> {
                    throw new UserAlreadyVotedException();
                });
    }

    private UserInfo getUserInfo(String userId) {
        try {
            return externalServiceAccess.getUserByUserId(userId);
        } catch (UserServiceException exception) {
            //assuming user api returns 404 for that request and the exception code defines user not found.
            throw new EntityNotFoundException("User not found for id " + userId);
        }
    }
}
