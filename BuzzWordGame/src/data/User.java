package data;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import components.AppDataComponent;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */



public class User implements AppDataComponent {
    private static int userIDcount = 0;

    private int         userID;
    private String      userName = "defaultUserName";
    private byte[]     password;
    private GameData    gameData;
    private byte[]      salt;

//    private Map<String, String> versions;
//
//    private String provider;

    public User(String initName, String initPW, GameData gameData){
        userIDcount++;
        userID = userIDcount;
        this.userName = initName;
        this.gameData = gameData;

        try {
            salt = generateSalt();
            this.password = getEncryptedPassword(initPW, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

    public User(){}

    public String getUserName(){
        return userName;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData){
        this.gameData = gameData;
    }

    public int getUserID() {
        return userID;
    }

    public byte[] getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

//    public static int getUserIDcount() {
//        return userIDcount;
//    }

    public static void setUserIDcount(int userIDcount) {
        User.userIDcount = userIDcount;
    }

    @Override
    public void reset() {

    }

    public byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException,InvalidKeySpecException {
        // these codes made reference to https://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html.
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        return secretKeyFactory.generateSecret(keySpec).getEncoded();
    }

    public byte[] generateSalt() throws NoSuchAlgorithmException{
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Encrypt the clear-text password using the same salt that was used to

        // encrypt the original password
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);



        // Authentication succeeds if encrypted password that the user entered

        // is equal to the stored hash

        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);

    }

}

