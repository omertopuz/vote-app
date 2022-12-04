package org.example.domain.service;

import org.example.domain.AppUtil;
import org.example.domain.exception.*;
import org.example.domain.model.*;
import org.example.ports.outbounds.EventPublisher;
import org.example.ports.outbounds.ExternalServiceAccess;
import org.example.ports.outbounds.VotePersistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @InjectMocks
    private VoteService voteService;
    @Mock
    private VotePersistence votePersistence;
    @Mock
    private ExternalServiceAccess externalServiceAccess;
    @Mock
    private EventPublisher eventPublisher;
    private final Date currentTime = new Date();
    private final UserInfo userInfo = new UserInfo("ABC123456","John Doe","john.doe@example.com");
    private final Election election = new Election(1, currentTime, new Date(currentTime.getTime() + AppUtil.DAY_TIME_SECONDS));
    private final Vote vote = new Vote(userInfo.getUserId(),election.getElectionId(),1,"LE_CIPHER");
    private final UserSecurityItem userSecurityItem = new UserSecurityItem(userInfo,true,null);

    @Test
    @DisplayName("Happy path for send vote use case")
    void should_return_voteResult_with_success_when_sendVote_called_verify_outbound_ports_called_once(){
        when(externalServiceAccess.getElectionById(any()))
                .thenReturn(election);
        when(externalServiceAccess.getUserByUserId(any())).thenReturn(userInfo);
        when(externalServiceAccess.checkUserWithSecurityItem(any(),anyString()))
                .thenReturn(userSecurityItem);
        VoteResult voteResult = voteService.sendVote(vote);
        assertNotNull(voteResult);
        assertTrue(voteResult.isSuccessful());
        verify(externalServiceAccess,times(1)).getElectionById(any());
        verify(votePersistence,times(1)).getUserVote(any());
        verify(votePersistence,times(1)).saveUserVote(any());
        verify(externalServiceAccess,times(1)).getUserByUserId(any());
        verify(externalServiceAccess,times(1)).checkUserWithSecurityItem(any(),any());
        verify(eventPublisher,times(1)).voteCastedEvent(any());
        verify(eventPublisher,times(1)).notifyUser(any());
    }

    @Test
    @DisplayName("sendVote executed with Invalid election id")
    void expect_EntityNotFoundException_when_sendVote_called_assuming_getElectionById_returns_null(){
        when(externalServiceAccess.getElectionById(any()))
                .thenThrow(ElectionServiceException.class);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, ()->voteService.sendVote(vote));
        assertTrue(exception.getMessage().contains(vote.getElectionId().toString()));
    }

    @Test
    @DisplayName("sendVote used for an election expired")
    void expect_VoteDomainException_when_sendVote_called_for_an_expired_election(){
        Election expiredElection = new Election(2, new Date(currentTime.getTime() - 2 * AppUtil.DAY_TIME_SECONDS), new Date(currentTime.getTime() - AppUtil.DAY_TIME_SECONDS));
        when(externalServiceAccess.getElectionById(any()))
                .thenReturn(expiredElection);
        VoteDomainException exception = assertThrows(VoteDomainException.class, ()->voteService.sendVote(vote));
        assertTrue(exception.getMessage().contains("over"));
    }

    @Test
    @DisplayName("sendVote called with invalid user id")
    void expect_EntityNotFoundException_when_sendVote_called_with_invalid_userId(){
        when(externalServiceAccess.getElectionById(any())).thenReturn(election);
        when(externalServiceAccess.getUserByUserId(any())).thenThrow(UserServiceException.class);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, ()->voteService.sendVote(vote));
        assertTrue(exception.getMessage().contains(vote.getUserId()));
    }

    @Test
    @DisplayName("sendVote called with invalid security item")
    void expect_SecurityItemInvalidException_when_sendVote_called_with_invalid_securityItem(){
        when(externalServiceAccess.getElectionById(any())).thenReturn(election);
        when(externalServiceAccess.getUserByUserId(any())).thenReturn(userInfo);
        when(externalServiceAccess.checkUserWithSecurityItem(any(),any())).thenThrow(SecurityItemInvalidException.class);

        assertThrows(SecurityItemInvalidException.class, ()->voteService.sendVote(vote));
    }

    @Test
    @DisplayName("user trying to re-vote")
    void expect_UserAlreadyVotedException_when_sendVote_called_but_user_already_voted(){
        when(externalServiceAccess.getElectionById(any())).thenReturn(election);
        when(externalServiceAccess.getUserByUserId(any())).thenReturn(userInfo);
        when(externalServiceAccess.checkUserWithSecurityItem(any(),anyString())).thenReturn(userSecurityItem);
        when(votePersistence.getUserVote(any())).thenReturn(Optional.of(new UserVote()));

        assertThrows(UserAlreadyVotedException.class, ()->voteService.sendVote(vote));
    }
}