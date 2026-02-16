package services;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class GameServices {
    MemoryAuthDAO auth;
    MemoryUserDAO dataaccess;
    MemoryGameDAO game;

    public GameServices(MemoryUserDAO userDataBase, MemoryAuthDAO authDataBase, MemoryGameDAO g){
        auth = authDataBase;
        dataaccess = userDataBase;
        game = g;
    }

    public Integer createGameServices(String gameName){
        return game.createGame(gameName);
    }

}
