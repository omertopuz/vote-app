package org.example.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.domain.model.Score;
import org.example.domain.model.ScoreRequest;
import org.example.ports.inbounds.ScoreUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "scores")
@RestController
@RequestMapping("/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreUseCase scoreUseCase;

    @GetMapping("/elections/{electionId}")
    public ResponseEntity<Score> getScore(@PathVariable Integer electionId){
        return new ResponseEntity<>(scoreUseCase.showScore(new ScoreRequest(electionId)), HttpStatus.OK);
    }

}
