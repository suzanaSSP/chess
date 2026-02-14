package dataaccess.interfaces;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    void clear();
    UserData getUser(String username);
    UserData createUser(String username, String password, String email);

}
