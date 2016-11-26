package controller;

import buzzword.BuzzWord;
import data.GameData;
import data.GameMode;
import data.User;
import gui.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static settings.AppPropertyType.SAVE_ERROR_TITLE;
import static settings.InitializationParameters.APP_WORKDIR_PATH;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */

public class BuzzWordController implements FileController {

    private BuzzWord            buzzWord;
    private GameData            gameData;
    private Text[]              progress;
    private Text[]              letters;
    private Circle[]            circles;
    private Line[]              lines;
    private DropShadow          effect;  //usded to setHightlight of shapes
    private User                user;


    private boolean             success;
    private boolean             gameover;

    private int                 discovered;
    private Button              playButton;
    private Button              pauseButton;

    private Label               timeRemaining;

    private File                workFile;

    private PropertyManager propertyManager;


    public BuzzWordController(BuzzWord buzzWord){

    }

    public void enablePlayButton(){

    }

    public void start(){
        gameData = new GameData(buzzWord);
        buzzWord.setDataComponent(gameData);


    }

    private void end(){

    }

    private void initializeCircles(HBox lettersZone){

    }

    private void initializeLetters(GridPane letters){

    }

    public void play(){

    }

    private boolean alreadyTried(char c){
        return false;
    }







    @Override
    public void handleNewRequest() {
    }

    @Override
    public void handleSaveRequest() throws IOException {

    }

    @Override
    public void handleLoadRequest() throws IOException {

        StackPane createUser = new StackPane();

        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(10);


        TextField username = new TextField();
        PasswordField passwordField = new PasswordField();
        Label wrong = new Label("Input Value cannot be empty");
        wrong.setVisible(false);
        Button login = new Button("Login");
        login.setStyle("-fx-font-size: 10pt; -fx-font-family: Segoe UI Light; -fx-text-fill: white; -fx-opacity: 1;");
        login.setStyle("-fx-pref-height: 30px; -fx-pref-width: 80px; -fx-background-color: rgb(183, 187, 191); -fx-background-radius: 6,5;");
        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-font-size: 10pt; -fx-font-family: Segoe UI Light; -fx-text-fill: white; -fx-opacity: 1;");
        cancel.setStyle("-fx-pref-height: 30px; -fx-pref-width: 80px; -fx-background-color: rgb(183, 187, 191); -fx-background-radius: 6,5;");
        pane.add(new Label("User Name"), 0, 0);
        pane.add(username, 1, 0);
        pane.add(new Label("Password"), 0,1);
        pane.add(passwordField, 1,1);
        pane.add(login, 0, 2);
        pane.add(cancel, 1, 2);
        pane.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(pane, wrong);
        vBox.setAlignment(Pos.CENTER);



        String name = username.getText();
        String pw = passwordField.getText();

        createUser.getChildren().setAll(vBox);
        Scene scene = new Scene(createUser, 300,200, Color.LIGHTGRAY);
        Stage stage = new Stage();
        stage.setScene(scene);
//        createUser.setPrefSize(300, 200);
//        Pane dia = new Pane(createUser);
//        pane.setPrefSize(300,200);

        stage.initOwner(buzzWord.getGUI().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);



//        Dialog dialog = new Dialog();
//        dialog.getDialogPane().getChildren().setAll(dia);
//        dialog.initOwner(buzzWord.getGUI().getWindow());
//        dialog.initModality(Modality.WINDOW_MODAL);
//        dialog.showAndWait();
        cancel.setOnAction(event -> {
            stage.close();
        });
        login.setOnAction(event -> {
            if (name.length()==0 || pw.length()==0){
                wrong.setVisible(true);
            }else {
                /* load codes here  or create a new method to load and call that here.*/
                stage.close();
            }

        });

        stage.showAndWait();

    }

    @Override
    public void handleExitRequest() {

    }

    private boolean handleNewRequestWhenLoading(){
        return false;
    }

    @Override
    public void handleCreatingNewUser(){
        StackPane createUser = new StackPane();

        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(10);


        TextField username = new TextField();
        PasswordField passwordField = new PasswordField();
        Label wrong = new Label("Input Value cannot be empty");
        wrong.setVisible(false);
        Button create = new Button("Create");
        create.setStyle("-fx-font-size: 10pt; -fx-font-family: Segoe UI Light; -fx-text-fill: white; -fx-opacity: 1;");
        create.setStyle("-fx-pref-height: 30px; -fx-pref-width: 80px; -fx-background-color: rgb(183, 187, 191); -fx-background-radius: 6,5;");
        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-font-size: 10pt; -fx-font-family: Segoe UI Light; -fx-text-fill: white; -fx-opacity: 1;");
        cancel.setStyle("-fx-pref-height: 30px; -fx-pref-width: 80px; -fx-background-color: rgb(183, 187, 191); -fx-background-radius: 6,5;");
        pane.add(new Label("User Name"), 0, 0);
        pane.add(username, 1, 0);
        pane.add(new Label("Password"), 0,1);
        pane.add(passwordField, 1,1);
        pane.add(create, 0, 2);
        pane.add(cancel, 1, 2);
        pane.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(pane, wrong);
        vBox.setAlignment(Pos.CENTER);






        createUser.getChildren().setAll(vBox);
        Scene scene = new Scene(createUser, 300,200, Color.LIGHTGRAY);
        Stage stage = new Stage();
        stage.setScene(scene);
//        createUser.setPrefSize(300, 200);
//        Pane dia = new Pane(createUser);
//        pane.setPrefSize(300,200);

        stage.initOwner(buzzWord.getGUI().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();


//        Dialog dialog = new Dialog();
//        dialog.getDialogPane().getChildren().setAll(dia);
//        dialog.initOwner(buzzWord.getGUI().getWindow());
//        dialog.initModality(Modality.WINDOW_MODAL);
//        dialog.showAndWait();
//        final String name = username.getText();
//        final String pw = passwordField.getText();
        Text name = new Text(username.getText());
        Text pw = new Text(passwordField.getText());

        cancel.setOnAction(event -> {
            stage.close();
        });


        create.setOnAction(event -> {
            name.setText(username.getText());
            pw.setText(passwordField.getText());
            if (name.getText().length()==0 || pw.getText().length()==0){
                wrong.setVisible(true);
            }else {
                wrong.setVisible(false);
                user = new User(name.getText(), pw.getText(), gameData);
                gameData.setUser(user);
                //gameData.setUser(name,pw);
                stage.close();
                WorkSpace w = (WorkSpace) buzzWord.getWorkspaceComponent();
                w.reinitializeAfterLogin(user);
            }

        });

        //stage.showAndWait();

        //WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        //workSpace.reinitializeAfterLogin(gameData.getUser());

    }

    public void handleLogoutRequest() { //will save data

    }


    public void handleMode0(String mode){
        GameMode gameMode = new GameMode(mode, gameData);
        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);
    }

    public void handleMode1(String mode){
        GameMode gameMode = new GameMode(mode, gameData);
        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);

    }

    public void handleMode2(String mode){
        GameMode gameMode = new GameMode(mode, gameData);
        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);

    }

    public void handleMode3(String mode){
        GameMode gameMode = new GameMode(mode, gameData);
        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);

    }

    private int prompToSave(){
        return 0;
    }

    private void save(File target){
        try {
            FileChooser fc = new FileChooser();
            URL workDirtURL = BuzzWord.class.getClassLoader().getResource(APP_WORKDIR_PATH.getParameter());
            File initialDir = new File(workDirtURL.getFile());
            fc.setInitialDirectory(initialDir);
            fc.setInitialFileName(user.getUserName()+".json");
            File selectedFile = fc.getInitialDirectory();
            buzzWord.getFileComponent().saveData(user, Paths.get(selectedFile.getAbsolutePath()));

        } catch (Exception e){
            AppMessageDialogSingleton dialogSingleton = AppMessageDialogSingleton.getSingleton();
            dialogSingleton.show(propertyManager.getPropertyValue(SAVE_ERROR_TITLE), propertyManager.getPropertyValue(SAVE_ERROR_MESSAGE));
        }


    }

    private void resetData(GameData data){

    }


    public void setBuzzWord(BuzzWord buzzword){
        this.buzzWord = buzzword;
    }



}
