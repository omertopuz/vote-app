package org.example.ports.outbounds;

import org.example.domain.exception.UserServiceException;
import org.example.domain.model.Election;
import org.example.domain.model.UserInfo;
import org.example.domain.model.UserSecurityItem;

public interface ExternalServiceAccess {

    UserInfo getUserByUserId(String userId) throws UserServiceException;
    UserSecurityItem checkUserWithSecurityItem(String userId, String securityItem);
    Election getElectionById(Integer electionId);

}
