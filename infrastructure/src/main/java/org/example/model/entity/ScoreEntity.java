package org.example.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "score")
@Getter
@Setter
public class ScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scoreId;
    private Integer electionId;
    private Integer candidateId;
    private int voteCount;
}
