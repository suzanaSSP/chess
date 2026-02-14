package dataaccess;

import dataaccess.interfaces.AuthDAO;

import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    public String createAuth(){
        return UUID.randomUUID().toString();
    }
    public String getAuth(){
        return "hi";
    }
    public void deleteAuth(){
        System.out.println("HI");
    }
}
