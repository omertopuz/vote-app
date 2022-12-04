package org.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {

    private String userId;
    private Integer electionId;
    private Integer candidateId;
    private String securityItem;
}
