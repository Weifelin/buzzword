package data;

import buzzword.BuzzWord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import components.AppDataComponent;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Red on 11/13/16.
 */
public class GameData implements AppDataComponent {
    private static final int TOTAL_NUMBER_OF_STORED_WORDS    = 330622;

    private GameMode                    mode;
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

    public void reset(){

    }

    public boolean isWord(String potentialWord){
        return false;
    }


}
