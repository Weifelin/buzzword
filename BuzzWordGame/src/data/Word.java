package data;

import javafx.scene.text.Text;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */

public class Word {
    private Text word;
    private int points;
    private Text point;

    public Word(String string){
        word = new Text(string);
        if (string.length()<=3){
            points = 10;
        } else {
            points = 10 + 10*(string.length()-3);
        }
        point = new Text(String.valueOf(points));
    }

    public Text getWord(){
        return word;
    }

    public Text getPoint(){
        return point;
    }

    public int getPoints(){
        return points;
    }
}
