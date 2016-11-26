package gui;


import buzzword.BuzzWord;
import components.AppWorkspaceComponent;
import controller.BuzzWordController;
import data.GameMode;
import data.Level;
import data.User;
import data.Word;
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
    Button                  HomeButton;
    VBox                    workBox;
    ScrollPane              scrollPane;

    GridPane                workArea;
    VBox                    rightSide;
    Label                   remainingTime;

    private Circle[]            circles;
    private Line[]              lines;
    private StackPane[]         stackPanes;
    private Text[]              texts;



    public WorkSpace(BuzzWord initApp){
        game = initApp;
        gui  = game.getGUI();
        layoutGUI();
        setupHandlers();
    }

    private void layoutGUI(){
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


        bodyPane.setCenter(workBox);

        workBox.setAlignment(Pos.CENTER);




    }

    private void setupHandlers() {
        BuzzWordController controller = (BuzzWordController) gui.getFileController();
        controller.setBuzzWord(game);
        controller.start();
    }



    @Override
    public void initStyle() {

    }

    @Override
    public void reloadWorkspace() {

    }

    public void reinitialize(){

    }

    public void reinitializeAfterLogin(User user){

        Button newProfile = gui.getNewProfileButton();
        Button login      = gui.getLoginButton();
        Button logout     = gui.getLogoutButton();
        MenuButton selectMode = gui.getSelectModesButton();
        Button  start     = gui.getStartGameButton();

        newProfile.setVisible(false);
        login.setVisible(false);

        logout.setText(user.getUserName());
        //logout.setText("UserName");
        selectMode.setVisible(true);
        start.setVisible(true);
        logout.setVisible(true);

    }

    public void reinitializeAfterModeSelection(GameMode mode){
        gui.getSelectModesButton().setVisible(false);
        gui.getStartGameButton().setVisible(false);
        Button home = gui.getHomeButton();
        home.setVisible(true);

        workBox.getChildren().set(0, mode.getLabel());

        Level[] levels = mode.getLevels();

        for (int i=0; i<texts.length; i++){
            if (i<levels.length){
                texts[i].setText(String.valueOf(i+1));
                Level level = levels[i];
                circles[i].setOnMouseClicked(event -> reinitializeAfterLevelSelection(level));
            } else {
                circles[i].setVisible(false);
                texts[i].setVisible(false);
            }
        }

    }

    public void reinitializeAfterLevelSelection(Level level){


        workArea.setAlignment(Pos.CENTER);
        modeName.setAlignment(Pos.CENTER);
        levelName.setAlignment(Pos.CENTER);
        startAndPauseButtonBox.setAlignment(Pos.CENTER);

        workBox.getChildren().set(2, level.getLabel());
        workBox.getChildren().get(3).setVisible(true);


        for (int i=0; i<circles.length;i++){
            circles[i].setVisible(true);
            texts[i].setVisible(true);
            Circle circle = circles[i];
            circles[i].setOnMouseClicked(event -> hightlight(circle));
        }

        for (int i=0; i< texts.length; i++){
            texts[i].setText(null);
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
            lines[i].setVisible(true);
        }

        rightSide = new VBox();
        remainingTimeBox = new HBox();
        startingLettersBox = new HBox();
        guessedWordsBox    = new HBox();

        wordBox             = new VBox();
        wordBox.setSpacing(5);
        pointsBox           = new VBox();
        pointsBox.setSpacing(5);
        scrollPane = new ScrollPane();
        scrollPane.setPrefSize(150,300);

        Word[] words = new Word[3];
        words[0] = new Word("WAR");
        words[1] = new Word("RAW");
        words[2] = new Word("DRAW");

        Text[] texts = new Text[2];
        texts[0] = new Text("B");
        texts[1] = new Text("U");

        rightSide.setSpacing(20);
        remainingTime = new Label("TimeRemaining: "+level.getRemainingTime()+" seconds");
        remainingTime.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: rgb(255, 132, 144); -fx-opacity: 1;");
        remainingTimeBox.getChildren().setAll(remainingTime);

        startingLettersBox.getChildren().add(texts[0]);
        startingLettersBox.getChildren().add(texts[1]);

        int point=0;
        for (int i=0; i<words.length; i++){
            wordBox.getChildren().add(words[i].getWord());
            pointsBox.getChildren().add(words[i].getPoint());
            point = point+words[i].getPoints();
        }

        Label total = new Label("Total");
        total.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: white; -fx-opacity: 1; -fx-background-color: rgb(183, 187, 191)");
        Label totalPoints = new Label(String.valueOf(point));
        totalPoints.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: white; -fx-opacity: 1; -fx-background-color: rgb(183, 187, 191)");

        total.setAlignment(Pos.BOTTOM_CENTER);
        totalPoints.setAlignment(Pos.BOTTOM_CENTER);
        wordBox.getChildren().add(total);
        pointsBox.getChildren().add(totalPoints);

        guessedWordsBox.getChildren().setAll(wordBox,pointsBox);
        scrollPane.setContent(guessedWordsBox);
        guessedWordsBox.setPrefSize(scrollPane.getPrefWidth(), scrollPane.getPrefHeight());
        wordBox.setPrefSize(guessedWordsBox.getPrefWidth()/2, guessedWordsBox.getPrefHeight());
        pointsBox.setPrefSize(guessedWordsBox.getPrefWidth()/2, guessedWordsBox.getPrefHeight());




        targetPoint = new VBox();
        Label target = new Label("Target");
        target.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill:white; -fx-opacity: 1;");
        Label targetPoints = new Label(String.valueOf(level.getTargetPoint()));
        targetPoints.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill:white; -fx-opacity: 1;");
        targetPoint.getChildren().setAll(target, targetPoints);
        rightSide.getChildren().setAll(remainingTimeBox, startingLettersBox, scrollPane, targetPoint);

        bodyPane.setRight(rightSide);


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
                    stackPanes[stackpaneIndex].getChildren().setAll(circles[circileIndex], texts[textIndex]);
                    textIndex++;
                    circileIndex++;

                } else if (isEven(row) && !isEven(column)){
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
}
