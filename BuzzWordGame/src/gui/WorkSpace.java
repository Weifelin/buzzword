package gui;


import buzzword.BuzzWord;
import components.AppWorkspaceComponent;
import data.GameMode;
import data.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Red on 11/13/16.
 */
public class WorkSpace extends AppWorkspaceComponent{
    BuzzWord                game;
    BuzzWordGUI             gui;
    BorderPane              bodyPane;
    VBox                    buttonlist;
    HBox                    remainingTimeBox;
    VBox                    guessedWordsBox;
    HBox                    startingLettersBox;
    VBox                    targetPoint;
    VBox                    workPane;
    HBox                    startAndPauseButtonBox;
    Label                   modeName;
    Label                   levelName;
    Button                  startGameButton;
    Button                  pauseGameButton;
    Button                  newProfileButton;
    Button                  loginButton;
    Button                  logoutButton;
    Button                  HomeButton;
    ComboBox                selectModesButton;
    ObservableList<String>  ModesList;


    public WorkSpace(BuzzWord initApp){
        game = initApp;
        gui  = game.getGUI();
    }

    private void layoutGUI(){

    }

    private void setupHandlers() {

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

    }

    public void reinitializeAfterModeSelection(GameMode mode){

    }
}
