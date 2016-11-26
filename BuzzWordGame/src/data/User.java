package data;


import components.AppDataComponent;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */


public class User implements AppDataComponent {
    private static int userIDcount = 0;

    private int         userID;
    private String      userName = "defaultUserName";
    private String      password;
    private GameData    gameData;

    public User(String initName, String initPW, GameData gameData){
        userIDcount++;
        userID = userIDcount;

        this.userName = initName;
        this.password = initPW;
        this.gameData = gameData;
    }


    public String getUserName(){
        return userName;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData){
        this.gameData = gameData;
    }


    @Override
    public void reset() {

    }
}

