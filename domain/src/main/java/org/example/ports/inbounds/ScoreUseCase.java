package org.example.ports.inbounds;

import org.example.domain.model.Score;
import org.example.domain.model.ScoreRequest;

public interface ScoreUseCase {
    Score showScore(ScoreRequest scoreRequest);
}
