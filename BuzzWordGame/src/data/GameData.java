package data;

import buzzword.BuzzWord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import components.AppDataComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */

public class GameData implements AppDataComponent {
    private static final int TOTAL_NUMBER_OF_STORED_WORDS    = 330622;


    @JsonInclude
    private ArrayList<GameMode>  modes;


    private String                      wordTry;
    private int                         totalPoints;
    private int                         targetPoints;
    private int                         timeRemaining;
    private ArrayList                   passedLevels;

    private Set<String>                 wordsSet;
    private Set<Word>                   guessedWords;
    private Set<Character>              letters;

    @JsonIgnore
    private User                        user;


    @JsonIgnore
    public BuzzWord             buzzWord;


    public ArrayList<GameMode> getModes(){
        return modes;
    }

    public String getWordTry() {
        return wordTry;
    }

    public ArrayList getPassedLevels() {
        return passedLevels;
    }

    public Set<String> getWordsSet() {
        return wordsSet;
    }

    public Set<Character> getLetters() {
        return letters;
    }



    public GameData(BuzzWord buzzWord) {
        this.buzzWord = buzzWord;
        modes = new ArrayList<>(4);
        for (int i=0;i< 4; i++){
            modes.add(new GameMode());
        }

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


    public void setModes(ArrayList<GameMode> modes) {
        this.modes = modes;
    }

    public void setMode(int i, GameMode gameMode) {
        modes.set(i, gameMode);

    }
}
