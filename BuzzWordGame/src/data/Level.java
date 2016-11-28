package data;

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
    private Label label;
    private GameMode mode;
    private int remainingTime;
    private int targetPoint;



    private PropertyManager propertyManager = PropertyManager.getManager();

    public Level(GameMode mode, int level){
        this.mode = mode;
        this.level = level+1;
        label = new Label("Level "+level);
        label.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
        label.setAlignment(Pos.CENTER);
        remainingTime = 12*(level);
        targetPoint = 20*(level);
    }

    public GameMode getMode(){
        return mode;
    }

    public Label getLabel(){
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
}
