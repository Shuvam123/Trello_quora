package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignoutBusinessService {

    @Autowired
    private UserDao userDao;

    /**
     * This method is used to sign up the given new user
     *
     * @param authToken The user details to be signed up
     * @return Uuid  The persisted sign-out  user details.
     * @throws SignOutRestrictedException This exception is thrown if the given username or email already exists
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity signout(String authToken) throws SignOutRestrictedException {

        //Check and throw SignOutRestrictedException if the user doesn't exist in the database
        UserAuthTokenEntity userAuthTokenEntity= userDao.getUserByAuthToken(authToken);
        if(userAuthTokenEntity==null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        final ZonedDateTime now = ZonedDateTime.now();
        userAuthTokenEntity.setLogoutAt(now);
        userDao.updateUserAuthToken(userAuthTokenEntity);

        return userDao.getUuidByAuthToken(authToken);
    }



}
