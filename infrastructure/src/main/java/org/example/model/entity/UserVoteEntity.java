package org.example.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_vote")
@Getter
@Setter
public class UserVoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userVoteId;
    private Integer electionId;
    private String userId;
    private Date createdAt;
}
