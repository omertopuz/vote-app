package org.example.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.domain.model.Vote;
import org.example.domain.model.VoteResult;
import org.example.ports.inbounds.VoteUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "votes")
@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteUseCase voteUseCase;

    @PostMapping
    public ResponseEntity<VoteResult> sendVote(@RequestBody Vote vote){
        VoteResult voteResult = voteUseCase.sendVote(vote);
        return new ResponseEntity<>(voteResult, HttpStatus.CREATED);
    }

}
