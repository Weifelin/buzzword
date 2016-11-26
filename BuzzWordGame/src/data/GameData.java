package data;

import buzzword.BuzzWord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import components.AppDataComponent;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */

public class GameData implements AppDataComponent {
    private static final int TOTAL_NUMBER_OF_STORED_WORDS    = 330622;

    private ArrayList<GameMode>         modes;
    private String                      wordTry;
    private int                         totalPoints;
    private int                         targetPoints;
    private int                         timeRemaining;
    private ArrayList                   passedLevels;

    private Set<String>                 wordsSet;
    private Set<Word>                   guessedWords;
    private Set<Character>              letters;

    private User                        user;


    @JsonIgnore
    public BuzzWord             buzzWord;



    public GameData(BuzzWord buzzWord) {
        this.buzzWord = buzzWord;
    }

    public GameData(){}

    public void addGuessedWord(String w){

    }

    public void countDownTimer(){

    }

    public void setUser(String initName, String initPW){
        this.user = new User(initName, initPW, this);
    }

    public User getUser(){
        return user;
    }
    public void reset(){

    }

    public boolean isWord(String potentialWord){
        return false;
    }


    public void setUser(User user) {
        this.user = user;
    }
}
