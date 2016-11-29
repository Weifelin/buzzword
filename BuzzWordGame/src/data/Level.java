package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import propertymanager.PropertyManager;

import static buzzword.BuzzWordProperties.HEADING_LABEL;

/**
 * Created by Weifeng Lin on 11/14/16.
 *
 * @author Weifeng Lin
 */
public class Level {
    private int level;

    @JsonIgnore
    private Label label;

    @JsonIgnore
    private GameMode mode;

    private int remainingTime;
    private int targetPoint;
    private  int wordAmount;



    private PropertyManager propertyManager = PropertyManager.getManager();

    public Level() {

    }


    public Level(GameMode mode, int targetlevel){
        this.mode = mode;
        this.level = targetlevel+1;
//        label = new Label("Level "+level);
//        label.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
//        label.setAlignment(Pos.CENTER);
        remainingTime = 12*(level);

        wordAmount = 2+ level;

        targetPoint = 39*(level)*wordAmount;
    }

    public GameMode getMode(){
        return mode;
    }

    public Label getLabel(){
        label = new Label("Level "+level);
        label.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    public int getRemainingTime(){
        return remainingTime;
    }

    public int getTargetPoint(){
        return targetPoint;
    }

    public int getLevel() {
        return level;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public int getWordAmount() {
        return wordAmount;
    }

    public void adjustTargetPoints(int totalScore){
        if (targetPoint > totalScore){
            targetPoint = totalScore -10;
        }

    }
}
