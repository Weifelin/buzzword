package data;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import propertymanager.PropertyManager;

import static buzzword.BuzzWordProperties.HEADING_LABEL;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */


public class GameMode {
    private String modeName;
    private Label label;
    private GameData data;
    private Level[] levels;
    private PropertyManager propertyManager = PropertyManager.getManager();

    public GameMode(String modeName, GameData data){
        this.modeName = modeName;
        label = new Label(modeName);
        label.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
        label.setAlignment(Pos.CENTER);
        this.data = data;
        levels = new Level[4];
        for (int i=0; i<levels.length; i++){
            levels[i] = new Level(this, i);
        }
    }

    public GameData getData(){
        return data;
    }
    public Label getLabel(){
        return label;
    }

    public Level[] getLevels(){
        return levels;
    }
}
