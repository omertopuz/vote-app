package org.example.adapters.outbound.api;

import org.example.domain.AppUtil;
import org.example.domain.exception.ElectionServiceException;
import org.example.domain.exception.UserServiceException;
import org.example.domain.model.Election;
import org.example.domain.model.UserInfo;
import org.example.domain.model.UserSecurityItem;
import org.example.ports.outbounds.ExternalServiceAccess;

import java.util.Date;

public class ExternalServiceAccessImpl implements ExternalServiceAccess {
    @Override
    public UserInfo getUserByUserId(String userId) throws UserServiceException {
        return new UserInfo(userId, "John Doe","john.doe@example.com");
    }

    @Override
    public UserSecurityItem checkUserWithSecurityItem(String userId, String securityItem) {
        return new UserSecurityItem(new UserInfo(userId, "John Doe","john.doe@example.com"), true,"");
    }

    @Override
    public Election getElectionById(Integer electionId) {
        if (electionId == null || electionId > 2)
            throw new ElectionServiceException();

        Date currentTime = new Date();
        Election electionValid = new Election();
        electionValid.setElectionId(electionId);
        electionValid.setStartDate(currentTime);
        electionValid.setEndDate(new Date(currentTime.getTime() + AppUtil.DAY_TIME_SECONDS));

        Election electionInValid = new Election();
        electionInValid.setElectionId(electionId);
        electionInValid.setStartDate(new Date(currentTime.getTime() - 2 * AppUtil.DAY_TIME_SECONDS));
        electionInValid.setEndDate(new Date(currentTime.getTime() - AppUtil.DAY_TIME_SECONDS));
        if (electionId == 1 )
            return electionValid;
        else return electionInValid;
    }
}
