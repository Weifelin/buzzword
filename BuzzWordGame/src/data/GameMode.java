package data;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.GameError;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import propertymanager.PropertyManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static buzzword.BuzzWordProperties.HEADING_LABEL;


/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */


public class GameMode {
    private static final int TOTAL_NUMBER_OF_STORED_WORDS    = 10;
    private static final int WORD_SET_SIZE    = 100;




    @JsonIgnore
    private Label label;


    @JsonIgnore
    private GameData data;

    @JsonIgnore
    private Level[] levels;




    @JsonIgnore
    private ArrayList<Word> wordsSet;


    private int levelPassed = 0;


    private String modeName;

    private PropertyManager propertyManager = PropertyManager.getManager();

    public GameMode(String modeName, GameData data){
        this.modeName = modeName;
//        label = new Label(modeName);
//        label.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
//        label.setAlignment(Pos.CENTER);
        this.data = data;
        levels = new Level[4];
        for (int i=0; i<levels.length; i++){
            levels[i] = new Level(this, i);
        }
        setWords(modeName);
        levelPassed = 0;
    }


    public GameMode(){}



    public ArrayList<Word> getWordsSet() {
        return wordsSet;
    }

    private void setWords(String modename) {
        wordsSet = new ArrayList<>(WORD_SET_SIZE);
        String potentialTarget;

        URL wordsResource = getClass().getClassLoader().getResource("words/"+modename+".txt");
        assert wordsResource != null;
        int toSkip = 0;
        for (int i=0; i< WORD_SET_SIZE; i++) {
            //int toSkip = new Random().nextInt(TOTAL_NUMBER_OF_STORED_WORDS);
            if (toSkip < TOTAL_NUMBER_OF_STORED_WORDS) {
                try (Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {

                    potentialTarget = lines.skip(toSkip).findFirst().get();

                    while (!isWord(potentialTarget)) {
                        //toSkip = new Random().nextInt(TOTAL_NUMBER_OF_STORED_WORDS);
                        toSkip++;
                        Stream<String> linesII = Files.lines(Paths.get(wordsResource.toURI()));
                        potentialTarget = linesII.skip(toSkip).findFirst().get();
                    }

                    while (wordsSet.contains(potentialTarget)){
                        toSkip++;
                        Stream<String> linesIII = Files.lines(Paths.get(wordsResource.toURI()));
                        potentialTarget = linesIII.skip(toSkip).findFirst().get();
                    }

                    wordsSet.add(new Word(potentialTarget));
                    toSkip++;


                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }else {
                break;
            }
        }
        //throw new GameError("Unable to load word list");
    }

    public GameData getData(){
        return data;
    }

    public Label getLabel(){

        label = new Label(modeName);
        label.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    public Level[] getLevels(){
        return levels;
    }

    private boolean isWord(String potentialTarget){

        if (potentialTarget.length() > 16)
            return false;

        for (int i=0; i<potentialTarget.length();i++){
            int asciiCode = (int) potentialTarget.charAt(i);
            if ((asciiCode<65 ) || asciiCode>122){
                return false;
            }else if (asciiCode > 90 && asciiCode < 97){
                return false;
            }
        }

        return true;
    }

    public int getLevelPassed() {
        return levelPassed;
    }

    public void setLevelPassed(int levelPassed) {
        this.levelPassed = levelPassed;
    }

    public void setData(GameData data) {
        this.data = data;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getModeName() {
        return modeName;
    }

    public void resetLevelsAndWords(){
        levels = new Level[4];
        for (int i=0; i<levels.length; i++){
            levels[i] = new Level(this, i);
        }
        setWords(modeName);
    }


}
