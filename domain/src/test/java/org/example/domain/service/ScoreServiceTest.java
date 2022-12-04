package org.example.domain.service;

import org.example.domain.exception.ElectionServiceException;
import org.example.domain.exception.EntityNotFoundException;
import org.example.domain.model.*;
import org.example.ports.outbounds.ExternalServiceAccess;
import org.example.ports.outbounds.VotePersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {
    @InjectMocks
    private ScoreService scoreService;
    @Mock
    private VotePersistence votePersistence;
    @Mock
    private ExternalServiceAccess externalServiceAccess;

    @Test
    void should_return_score_when_showScore(){
        Score score = new Score(1, List.of(new ScoreCandidate(1,123)
        ,new ScoreCandidate(2,213)
        ,new ScoreCandidate(3,345)));
        when(votePersistence.getScore(any())).thenReturn(score);
        when(externalServiceAccess.getElectionById(any())).thenReturn(new Election());
        Score scoreFetched = scoreService.showScore(new ScoreRequest(1));
        assertNotNull(scoreFetched);
        assertTrue(scoreFetched.getScoreCandidates().size()>0);
        verify(votePersistence,times(1)).getScore(any());
    }

    @Test
    void expect_EntityNotFoundException_when_showScore_called_assuming_getElectionById_returns_null(){
        ScoreRequest scoreRequest = new ScoreRequest(1);
        when(externalServiceAccess.getElectionById(any()))
                .thenThrow(ElectionServiceException.class);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, ()->scoreService.showScore(new ScoreRequest(1)));
        assertTrue(exception.getMessage().contains(scoreRequest.getElectionId().toString()));
    }

    @Test
    void should_update_score_when_voteCastedEventListen(){
        scoreService.voteCastedEventListen(new VoteCastEventModel());
        verify(votePersistence,times(1)).updateScore(any(),any());
    }
}