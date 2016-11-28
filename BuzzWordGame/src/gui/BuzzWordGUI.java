package gui;

import apptemplate.AppTemplate;
import buzzword.BuzzWord;
import controller.BuzzWordController;
import controller.FileController;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import propertymanager.PropertyManager;
import settings.AppPropertyType;
import ui.AppGUI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static buzzword.BuzzWordProperties.ROOT_BORDERPANE_ID;
import static settings.AppPropertyType.*;
import static settings.InitializationParameters.APP_IMAGEDIR_PATH;
import static buzzword.BuzzWordProperties.*;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */
public class BuzzWordGUI extends AppGUI {

    //Button                    quitButton;
    Label                       guiHeadingLabel;
    Button                      startGameButton;
    Button                      resumeGameButton;
    Button                      pauseGameButton;
    Button                      newProfileButton;
    Button                      loginButton;
    Button                      logoutButton;
    Button                      homeButton;
    MenuButton                  selectModesButton;

    MenuItem[]                  modesList;

    HBox                        buttom;
    BorderPane                  heading;
    VBox                        buttonlist;
    private double x = 0;
    private double y = 0;



    public BuzzWordGUI(){
    }

    public BuzzWordGUI(Stage initPrimaryStage, String initAppTitle, BuzzWord buzzWord) throws IOException, InstantiationException {
        this(initPrimaryStage, initAppTitle, buzzWord, -1, -1);
    }

    public BuzzWordGUI(Stage primaryStage, String applicationTitle, BuzzWord buzzWord, int appSpecificWindowWidth, int appSpecificWindowHeight) throws IOException, InstantiationException {
        this.appSpecificWindowWidth = appSpecificWindowWidth;
        this.appSpecificWindowHeight = appSpecificWindowHeight;
        this.primaryStage = primaryStage;
        this.applicationTitle = applicationTitle;
        //initializeToolbar();                    // initialize the top
        initializeButtons();
        initializeHandlers(buzzWord); // set the toolbar button handlers
        initializeWindow();                     // start the app window (without the application-specific workspace)

    }


    protected void initializeButtons() throws IOException{
        PropertyManager propertyManager = PropertyManager.getManager();
        exitButton = new Button();
        initializeButton(exitButton, EXIT_ICON.toString(), EXIT_TOOLTIP.toString(), false);
        newProfileButton = new Button(propertyManager.getPropertyValue(CREATE_NEW_PROFILE_BUTTON));
        initSideButtons(newProfileButton, true);
        loginButton     = new Button(propertyManager.getPropertyValue(LOGIN));
        initSideButtons(loginButton, true);

        logoutButton    = new Button();
        //logoutButton.setVisible(false);
        //logoutButton.setStyle("-fx-pref-height: 40px; -fx-pref-width: 200px; -fx-background-color: rgb(183, 187, 191); -fx-background-radius: 6,5;" );

        initSideButtons(logoutButton, false);

        selectModesButton = new MenuButton(propertyManager.getPropertyValue(SELECT_MODE));
        selectModesButton.setVisible(false);


        modesList = new MenuItem[4];
        modesList[0] = new MenuItem(propertyManager.getPropertyValue(ENG_DIC));
        modesList[1] = new MenuItem(propertyManager.getPropertyValue(PLACES));
        modesList[2] = new MenuItem(propertyManager.getPropertyValue(SCIENCE));
        modesList[3] = new MenuItem(propertyManager.getPropertyValue(FAMOUS_PEOPLE));

        for (int i=0; i<modesList.length; i++){
            selectModesButton.getItems().add(modesList[i]);
        }

        startGameButton = new Button(propertyManager.getPropertyValue(START_PLAYING));
        initSideButtons(startGameButton, false);

        homeButton = new Button(propertyManager.getPropertyValue(HOME));
        initSideButtons(homeButton, false);


        buttonlist = new VBox();

        buttonlist.getChildren().addAll(newProfileButton, loginButton, logoutButton, selectModesButton, startGameButton, homeButton);
        buttonlist.setSpacing(10);

        resumeGameButton = new Button();
        pauseGameButton = new Button();
        initializeButton(resumeGameButton, RESUME_ICON.toString(), RESUME_TOOLTIP.toString(), false);
        initializeButton(pauseGameButton, PAUSE_ICON.toString(), PAUSE_TOOLTIP.toString(), false);


    }


    private void initializeHandlers(BuzzWord app) throws InstantiationException{
        try {
            Method getFileControllerClassMethod = app.getClass().getMethod("getFileControllerClass");
            String         fileControllerClassName      = (String) getFileControllerClassMethod.invoke(app);
            Class<?>       klass                        = Class.forName("controller." + fileControllerClassName);
            Constructor<?> constructor                  = klass.getConstructor(BuzzWord.class);
            fileController = (FileController) constructor.newInstance(app);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }


        BuzzWordController buzzWordController = (BuzzWordController) fileController;
        newProfileButton.setOnAction(event -> buzzWordController.handleCreatingNewUser());
        loginButton.setOnAction(event -> {
            try {
                buzzWordController.handleLoadRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        logoutButton.setOnAction(event -> {
            buzzWordController.handleLogoutRequest();
        });

        homeButton.setOnAction(event -> buzzWordController.handleReturnHomeReuqest());
        modesList[0].setOnAction(event -> buzzWordController.handleMode0(modesList[0].getText()));
        modesList[1].setOnAction(event -> buzzWordController.handleMode1(modesList[1].getText()));
        modesList[2].setOnAction(event -> buzzWordController.handleMode2(modesList[2].getText()));
        modesList[3].setOnAction(event -> buzzWordController.handleMode3(modesList[3].getText()));

        pauseGameButton.setOnAction(event -> buzzWordController.handlePauseRequest());
        resumeGameButton.setOnAction(event -> buzzWordController.handleResumeRequest());
        exitButton.setOnAction(event -> buzzWordController.handleExitRequest());

    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    @Override
    protected void initializeWindow() throws IOException {
        PropertyManager propertyManager = PropertyManager.getManager();
        guiHeadingLabel = new Label(propertyManager.getPropertyValue(WORKSPACE_HEADING_LABEL));
        guiHeadingLabel.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));

        heading = new BorderPane();
        heading.setRight(exitButton);
        heading.setCenter(guiHeadingLabel);

        // SET THE WINDOW TITLE
        primaryStage.setTitle(applicationTitle);
        primaryStage.initStyle(StageStyle.UNDECORATED);



        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();


        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX((bounds.getMaxX()-bounds.getMinX())/8);
        primaryStage.setY((bounds.getMaxY()-bounds.getMinY())/8);
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setResizable(true);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(720);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1000);

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        appPane.setTop(heading);
        heading.setPadding(new Insets(5, 0, 5, 0));
        appPane.setLeft(buttonlist);
        buttonlist.setPadding(new Insets(5,20, 5, 5));
        appPane.setPadding(new Insets(10,20,20,20));



        primaryScene = appSpecificWindowWidth < 1 || appSpecificWindowHeight < 1 ? new Scene(appPane)
                : new Scene(appPane,
                appSpecificWindowWidth,
                appSpecificWindowHeight);

        appPane.getStyleClass().add(propertyManager.getPropertyValue(ROOT_BORDERPANE_ID));
        //primaryScene.getStylesheets().add("css/buzzword_style.css");


        URL imgDirURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGEDIR_PATH.getParameter());
        if (imgDirURL == null)
            throw new FileNotFoundException("Image resrouces folder does not exist.");
        try (InputStream appLogoStream = Files.newInputStream(Paths.get(imgDirURL.toURI()).resolve(propertyManager.getPropertyValue(APP_LOGO)))) {
            primaryStage.getIcons().add(new Image(appLogoStream));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    private void initializeButton(Button button, String icon, String tooltip, boolean disabled) throws IOException{
        PropertyManager propertyManager = PropertyManager.getManager();

        URL imgDirURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGEDIR_PATH.getParameter());
        if (imgDirURL == null)
            throw new FileNotFoundException("Image resources folder does not exist.");

        try (InputStream imgInputStream = Files.newInputStream(Paths.get(imgDirURL.toURI()).resolve(propertyManager.getPropertyValue(icon)))) {
            Image buttonImage = new Image(imgInputStream);
            button.setDisable(disabled);
            button.setGraphic(new ImageView(buttonImage));
            Tooltip buttonTooltip = new Tooltip(propertyManager.getPropertyValue(tooltip));
            button.setTooltip(buttonTooltip);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void initSideButtons(Button button, boolean visible) {
        PropertyManager propertyManager = PropertyManager.getManager();


        button.setVisible(visible);
//        Rectangle rectangle = new Rectangle(Double.parseDouble(propertyManager.getPropertyValue(APP_BUTTON_LENGTH)),
//                Double.parseDouble(propertyManager.getPropertyValue(APP_BUTTON_WIDTH)),
//                Color.valueOf("#b7bbbf"));
//        rectangle.setArcWidth(Double.parseDouble(propertyManager.getPropertyValue(APP_BUTTON_ROUND)));
//        rectangle.setArcHeight(Double.parseDouble(propertyManager.getPropertyValue(APP_BUTTON_ROUND)));
//        button.setStyle(propertyManager.getPropertyValue(BUTTON_COLOR));
//
//
//        button.setShape(rectangle);
        //button.getStyleClass().setAll(propertyManager.getPropertyValue(BUTTON_COLOR));
        button.getStyleClass().setAll(propertyManager.getPropertyValue(TEXT_LABEL));
        button.setTextAlignment(TextAlignment.CENTER);
        button.setStyle("-fx-pref-height: 40px; -fx-pref-width: 200px; -fx-background-color: rgb(183, 187, 191); -fx-background-radius: 6,5;" );


    }


    public Button getNewProfileButton(){
        return newProfileButton;
    }

    public Button getLoginButton(){
        return loginButton;
    }

    public Button getLogoutButton(){
        return logoutButton;
    }

    public MenuButton getSelectModesButton(){
        return selectModesButton;
    }

    public Button getStartGameButton(){
        return startGameButton;
    }

    public Button getHomeButton(){
        return homeButton;
    }

    public Button getResumeButton(){
        return resumeGameButton;
    }

    public Button getPauseButton(){
        return pauseGameButton;
    }
}
