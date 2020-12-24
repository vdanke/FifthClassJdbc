package org.step.service;

import org.step.jdbc.UserDao;
import org.step.model.User;

public class UserService {

    private UserDao userDao = new UserDao();


    public User login(String username) {
        User login = null;
        try {
            login = userDao.login(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return login;
    }
}
