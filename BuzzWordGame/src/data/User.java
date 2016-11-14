package data;

import components.AppDataComponent;

/**
 * Created by Red on 11/13/16.
 */
public class User {
    private static int userIDcount = 0;

    private int         userID;
    private String      userName;
    private String      password;
    private GameData    gameData;

    public User(String initName, String initPW, GameData gameData){
        userIDcount++;
        userID = userIDcount;

        this.userName = initName;
        this.password = initPW;
        this.gameData = gameData;
    }


}
