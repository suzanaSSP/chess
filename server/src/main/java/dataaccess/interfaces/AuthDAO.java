package dataaccess.interfaces;

import model.AuthData;

public interface AuthDAO {
    void clear();
    String createAuth(String username);
    AuthData getAuth(String username);
    void deleteAuth(String username);
}
