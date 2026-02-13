package dataaccess.interfaces;

import model.UserData;

public interface UserDAO {
    void clear();
    UserData getUser(String username);
    UserData createUser(String username, String password, String email);

}
