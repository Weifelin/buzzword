package gui;


import buzzword.BuzzWord;
import components.AppWorkspaceComponent;
import controller.BuzzWordController;
import data.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */


public class WorkSpace extends AppWorkspaceComponent{
    BuzzWord                game;
    BuzzWordGUI             gui;
    BorderPane              bodyPane;
    VBox                    buttonlist;
    HBox                    remainingTimeBox;
    HBox                    guessedWordsBox;
    HBox                    startingLettersBox;
    VBox                    targetPoint;
    VBox                    wordBox;
    VBox                    pointsBox;


    HBox                    startAndPauseButtonBox;
    Label                   modeName;
    Label                   levelName;
    Button                  resumeGameButton;
    Button                  pauseGameButton;
    Button                  newProfileButton;
    Button                  loginButton;
    Button                  logoutButton;
    Button                  homeButton;
    Button                  startPlaying;
    Button                  helpButton;

    MenuButton              selectMode;

    VBox                    workBox;
    ScrollPane              scrollPane;

    GridPane                workArea;
    VBox                    rightSide;
    Label                   remainingTime;

    private Circle[]            circles;
    private Line[]              lines;
    private StackPane[]         stackPanes;
    private Text[]              texts;
    private Text[]              textsWords;

    private ArrayList<Word>  wordsSet;
    private ArrayList<Word>  guessedWord;
    private ArrayList<Word>     wordSequenceToGuess;



    public WorkSpace(BuzzWord initApp){
        game = initApp;
        gui  = game.getGUI();
        layoutGUI();
        setupHandlers();
    }

    private void layoutGUI(){
        newProfileButton = gui.getNewProfileButton();
        loginButton     = gui.getLoginButton();
        logoutButton     = gui.getLogoutButton();
        selectMode = gui.getSelectModesButton();
        startPlaying     = gui.getStartGameButton();
        homeButton = gui.getHomeButton();
        helpButton = gui.getHelpButton();

        bodyPane = gui.getAppPane();
        workBox = new VBox(10);
        initGrid();

        modeName = new Label();
        modeName.setVisible(false);
        levelName = new Label();
        levelName.setVisible(false);

        pauseGameButton = gui.getPauseButton();
        resumeGameButton = gui.getResumeButton();
        resumeGameButton.setVisible(false);

        startAndPauseButtonBox = new HBox(resumeGameButton, pauseGameButton);
        startAndPauseButtonBox.setVisible(false);

        workBox.getChildren().setAll(modeName, workArea, levelName, startAndPauseButtonBox);
        //workBox.getChildren().set(1, workArea);

        rightSide = new VBox();
        bodyPane.setCenter(workBox);
        bodyPane.setRight(rightSide);

        workBox.setAlignment(Pos.CENTER);




    }

    private void setupHandlers() {
        BuzzWordController controller = (BuzzWordController) gui.getFileController();
        controller.setBuzzWord(game);
        controller.start();
        controller.setWorkSpace(this);
    }



    @Override
    public void initStyle() {

    }

    @Override
    public void reloadWorkspace() {

    }

    public void reinitializeAfterLogout(){
        reinitializeToHome();
        logoutButton.setVisible(false);
        selectMode.setVisible(false);
        startPlaying.setVisible(false);
        newProfileButton.setVisible(true);
        loginButton.setVisible(true);
    }


    public void reinitializeToHome(){

        for (int i=0; i<circles.length;i++){
            circles[i].setVisible(true);
            texts[i].setVisible(true);
            //Circle circle = circles[i];
            //circles[i].setOnMouseClicked(event -> hightlight(circle));
            circles[i].setOnMouseClicked(null);
            deHighlight(circles[i]);
        }

        for (int i=0; i< texts.length; i++){
            texts[i].setText(null);
        }

        for (int i=0; i< textsWords.length; i++){
            textsWords[i].setVisible(false);
        }

        texts[0].setText("B");
        texts[1].setText("U");
        texts[4].setText("Z");
        texts[5].setText("Z");
        texts[10].setText("W");
        texts[11].setText("O");
        texts[14].setText("R");
        texts[15].setText("D");


        for (int i=0; i<lines.length; i++){
            lines[i].setVisible(false);
        }



        rightSide.setVisible(false);


        //setKey Back

//        modeName.setVisible(false);
//        levelName.setVisible(false);
//        startAndPauseButtonBox.setVisible(false);
        workBox.getChildren().get(0).setVisible(false);//mode name
        workBox.getChildren().get(2).setVisible(false);//level name
        workBox.getChildren().get(3).setVisible(false);//start pause

        logoutButton.setVisible(true);
        homeButton.setVisible(false);
        selectMode.setVisible(true);
        startPlaying.setVisible(true);
        helpButton.setVisible(false);

    }

    public void reinitializeAfterLogin(User user){

        newProfileButton = gui.getNewProfileButton();
        loginButton     = gui.getLoginButton();
        logoutButton     = gui.getLogoutButton();
        selectMode = gui.getSelectModesButton();
        startPlaying     = gui.getStartGameButton();

        newProfileButton.setVisible(false);
        loginButton.setVisible(false);

        logoutButton.setText(user.getUserName());
        //logout.setText("UserName");
        selectMode.setVisible(true);
        startPlaying.setVisible(true);
        logoutButton.setVisible(true);

    }

    public void reinitializeAfterModeSelection(GameMode mode){
        gui.getSelectModesButton().setVisible(false);
        gui.getStartGameButton().setVisible(false);

        homeButton = gui.getHomeButton();
        homeButton.setVisible(true);

        workBox.getChildren().set(0, mode.getLabel());

        Level[] levels = mode.getLevels();

        for (int i=0; i<texts.length; i++){
            if (i<levels.length){
                texts[i].setText(String.valueOf(i+1));
                //Level level = levels[i];
                //circles[i].setOnMouseClicked(event -> reinitializeAfterLevelSelection(level));
            } else {
                circles[i].setVisible(false);
                texts[i].setVisible(false);
            }
        }

        int levelPassed = mode.getLevelPassed();

        BuzzWordController controller = (BuzzWordController) gui.getFileController();


        for (int i=0; i<=levelPassed; i++){
            circles[i].setFill(Color.AQUA);
            Level level = levels[i];
            //circles[i].setOnMouseClicked(event -> reinitializeAfterLevelSelection(level));
            circles[i].setOnMouseClicked(event -> controller.play(level) );
        }


    }

    public void reinitializeAfterLevelSelection(Level level) {


        workArea.setAlignment(Pos.CENTER);
        modeName.setAlignment(Pos.CENTER);
        levelName.setAlignment(Pos.CENTER);
        startAndPauseButtonBox.setAlignment(Pos.CENTER); // resume , pause
        startAndPauseButtonBox.getChildren().get(0).setVisible(false);
        startAndPauseButtonBox.getChildren().get(1).setVisible(true);
        helpButton.setVisible(true);

        workBox.getChildren().set(2, level.getLabel());
        workBox.getChildren().get(3).setVisible(true);

        rightSide.setVisible(true);



        for (int i = 0; i < circles.length; i++) {
            circles[i].setVisible(true);
            texts[i].setVisible(false);
            textsWords[i].setVisible(true);
            Circle circle = circles[i];
            circles[i].setFill(Color.GRAY);

            circles[i].setOnMouseClicked(null);
        }

        for (int i = 0; i < textsWords.length; i++) {
            textsWords[i].setText(null);
        }

        textsWords[0].setText("B");
        textsWords[1].setText("U");
        textsWords[4].setText("Z");
        textsWords[5].setText("Z");
        textsWords[10].setText("W");
        textsWords[11].setText("O");
        textsWords[14].setText("R");
        textsWords[15].setText("D");

        wordsSet = level.getMode().getWordsSet(); // all words
        wordSequenceToGuess = new ArrayList<>();
        int wordamount = level.getWordAmount();

        int wordIndex;
        //Word word;

        ArrayList<Integer> has = new ArrayList<>();
        has.add(-1);

        for (int i = 0; i < wordamount; i++) {
            wordIndex = new Random().nextInt(wordsSet.size());
            while (has.contains(wordIndex)) {
                wordIndex = new Random().nextInt(wordsSet.size());
            }
            //word = wordsSet.get(wordIndex);
            has.add(wordIndex);
            wordSequenceToGuess.add(wordsSet.get(wordIndex));
        }

        //mess up the word
        char[] wordchar = wordSequenceToGuess.get(0).getWordvalue().toCharArray();
        //char[] table = new char[textsWords.length];
        ArrayList<Integer> added = new ArrayList<>(wordchar.length);
        for (int i = 0; i < wordchar.length; i++) {
            added.add(-1);
        }

        for (int i = 0; i < wordchar.length; i++) {
            int charIndex = new Random().nextInt(textsWords.length);
            while (added.contains(charIndex)) {
                charIndex = new Random().nextInt(textsWords.length);
            }
            added.set(i, charIndex);
            textsWords[charIndex].setText(String.valueOf(wordchar[i]));
        }


        for (int i = 0; i < lines.length; i++) {
            lines[i].setVisible(true);
        }


        //rightSide = new VBox();
        remainingTimeBox = new HBox();
        startingLettersBox = new HBox();
        guessedWordsBox = new HBox();



        wordBox = new VBox();
        wordBox.setSpacing(5);
        pointsBox = new VBox();
        pointsBox.setSpacing(5);
        scrollPane = new ScrollPane();
        scrollPane.setPrefSize(170, 300);


        guessedWord = new ArrayList<>();

//        Word[] words = new Word[3];
//        words[0] = new Word("WAR");
//        words[1] = new Word("RAW");
//        words[2] = new Word("DRAW");

        Text[] texts = new Text[2];
        texts[0] = new Text(String.valueOf(wordchar[0]));
        texts[1] = new Text(String.valueOf(wordchar[1]));

        rightSide.setSpacing(20);
        remainingTime = new Label("TimeRemaining: " +"\n"+ level.getRemainingTime() + " seconds");
        remainingTime.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: rgb(255, 132, 144); -fx-opacity: 1;");
        remainingTimeBox.getChildren().setAll(remainingTime);

        startingLettersBox.getChildren().add(texts[0]);
        startingLettersBox.getChildren().add(texts[1]);
        startingLettersBox.setVisible(false);

        int point = 0;

        //hard coded guessed word.
        for (int i = 0; i < wordSequenceToGuess.size(); i++) {
            guessedWord.add(wordSequenceToGuess.get(i));
        }

        //guessedWord.add(new Word("go"));

        for (int i = 0; i < guessedWord.size(); i++) {
            wordBox.getChildren().add(guessedWord.get(i).getWord());
            pointsBox.getChildren().add(guessedWord.get(i).getPoint());
            point = point + guessedWord.get(i).getPoints();
        }

        wordBox.setVisible(false);
        pointsBox.setVisible(false);

        Label total = new Label("Total");
        total.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: white; -fx-opacity: 1; -fx-background-color: rgb(183, 187, 191)");
        Label totalPoints = new Label(String.valueOf(point));
        totalPoints.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: white; -fx-opacity: 1; -fx-background-color: rgb(183, 187, 191)");

        total.setAlignment(Pos.BOTTOM_CENTER);
        totalPoints.setAlignment(Pos.BOTTOM_CENTER);
        wordBox.getChildren().add(total);
        pointsBox.getChildren().add(totalPoints);

        guessedWordsBox.getChildren().setAll(wordBox, pointsBox);
        scrollPane.setContent(guessedWordsBox);
        guessedWordsBox.setPrefSize(scrollPane.getPrefWidth(), scrollPane.getPrefHeight());
        wordBox.setPrefSize(guessedWordsBox.getPrefWidth() / 2, guessedWordsBox.getPrefHeight());
        pointsBox.setPrefSize(guessedWordsBox.getPrefWidth() / 2, guessedWordsBox.getPrefHeight());


        targetPoint = new VBox();
        level.adjustTargetPoints(point);
        Label target = new Label("Target");
        target.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill:white; -fx-opacity: 1;");
        Label targetPoints = new Label(String.valueOf(level.getTargetPoint()));
        targetPoints.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill:white; -fx-opacity: 1;");
        targetPoint.getChildren().setAll(target, targetPoints);
        rightSide.getChildren().setAll(remainingTimeBox, startingLettersBox, scrollPane, targetPoint);

        //bodyPane.setRight(rightSide);


    }

    private void initGrid(){
        workArea = new GridPane();
        workArea.setHgap(2);
        workArea.setVgap(2);
        workArea.setPrefSize(40, 40);


        texts = new Text[16];
        for (int i=0; i< texts.length; i++){
            texts[i] = new Text(null);
        }
        texts[0].setText("B");
        texts[1].setText("U");
        texts[4].setText("Z");
        texts[5].setText("Z");
        texts[10].setText("W");
        texts[11].setText("O");
        texts[14].setText("R");
        texts[15].setText("D");

        textsWords = new Text[16];
        for (int i=0; i< textsWords.length; i++){
            textsWords[i] = new Text(null);
            textsWords[i].setVisible(false);
        }

        circles = new Circle[16];

        for (int i=0; i<circles.length; i++){
            circles[i] = new Circle(30, Color.GRAY);
        }

        lines = new Line[24];

        for (int i=0; i<lines.length; i++){
            lines[i] = new Line(0,20, 40,20);
            lines[i].setStrokeWidth(2);
            lines[i].setStroke(Color.BLACK);
            lines[i].setVisible(false);
        }

        stackPanes = new StackPane[49];
        for (int i=0; i<stackPanes.length; i++){
            stackPanes[i] = new StackPane();
        }

        int textIndex = 0;
        int stackpaneIndex = 0;
        int linesIndex = 0;
        int  circileIndex = 0;
        for (int row =0; row < 7; row ++){

            for (int column=0; column<7; column++){
                if (isEven(column) && isEven(row)){
                    stackPanes[stackpaneIndex].getChildren().setAll(circles[circileIndex], texts[textIndex], textsWords[textIndex]);
                    textIndex++;
                    circileIndex++;

                } else if (isEven(row) && !isEven(column)){
                    // horizontal
                    stackPanes[stackpaneIndex].getChildren().setAll(lines[linesIndex]);

                    linesIndex++;
                } else if ( !isEven(row) && isEven(column)){
                    lines[linesIndex].setStartX(20);
                    lines[linesIndex].setStartY(0);
                    lines[linesIndex].setEndX(20);
                    lines[linesIndex].setEndY(40);
                    stackPanes[stackpaneIndex].getChildren().setAll(lines[linesIndex]);
                    linesIndex++;

                }
                workArea.add(stackPanes[stackpaneIndex], column, row);
                stackpaneIndex++;
            }
        }




    }

    private boolean isEven(int i){
        if (i%2 == 0)
            return true;
        else
            return false;
    }

    private void hightlight(Shape shape){
        DropShadow dropShadow = new DropShadow(3, Color.AQUA);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setHeight(50);
        dropShadow.setHeight(50);

        shape.setEffect(dropShadow);
    }

    private void deHighlight(Shape shape){
        shape.setEffect(null);
    }

    public Text[] getTextsWords() {
        return textsWords;
    }

    public VBox getWordBox() {
        return wordBox;
    }

    public VBox getPointsBox() {
        return pointsBox;
    }

    public HBox getStartingLettersBox() {
        return startingLettersBox;
    }

    public VBox getTargetPoint() {
        return targetPoint;
    }

    public Label getRemainingTime() {
        return remainingTime;
    }

    public ArrayList<Word> getGuessedWord() {
        return guessedWord;
    }

    public Circle[] getCircles() {
        return circles;
    }

    public ArrayList<Word> getWordsSet() {
        return wordsSet;
    }

    public ArrayList<Word> getWordSequenceToGuess() {
        return wordSequenceToGuess;
    }

    public Line[] getLines() {
        return lines;
    }
}
