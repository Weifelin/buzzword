package controller;

import buzzword.BuzzWord;
import data.*;
import gui.WorkSpace;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;
import ui.YesNoCancelDialogSingleton;

import javafx.scene.input.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static buzzword.BuzzWordProperties.PLACES;
import static settings.AppPropertyType.*;
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

    private int                 selection;


    private boolean             success;
    private boolean             gameover;

    private int                 discovered;
    private Button              resumeButton;
    private Button              pauseButton;
    private int                 currentLevelIndex;

    private Label               timeRemaining;

    private File                workFile;

    private PropertyManager propertyManager;

    private Text[]              textsWords;
    private ArrayList<Word>    wordsSet;
    private ArrayList<Word>  guessedWord;
    private ArrayList<Word>     wordSequenceToGuess;
    //private char[]                      wordchar;
    private ArrayList<String>         wordSequenceString;
    private Set<Character>      guessingWord;

    private  WorkSpace          workSpace;

    HBox                    startingLettersBox;
    VBox                    wordBox;
    VBox                    pointsBox;
    VBox                    targetPoint;
    Label                   remainingTime;
    private Level            currentLevel;
    private int             time;
    private Timer           countdownTimer;
    private int                 point;


    public BuzzWordController(BuzzWord buzzWord){

    }

    public void enablePlayButton(){

    }

    public void start(){
        gameData = new GameData(buzzWord);
        User defaultUser = new User("defaultUser", "123", gameData);
        user = defaultUser;
        gameData.setUser(user);
        buzzWord.setDataComponent(user);

        resumeButton = buzzWord.getGUI().getResumeButton();
        pauseButton = buzzWord.getGUI().getPauseButton();

        //workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();


    }

    private void end(){
        PropertyManager propertyManager = PropertyManager.getManager();

        Platform.runLater(()->{

            if (success == true){
                AppMessageDialogSingleton dialogSingleton = AppMessageDialogSingleton.getSingleton();
                dialogSingleton.showEnd(propertyManager.getPropertyValue(WIN_LABEL_TITLE), propertyManager.getPropertyValue(WIN_LABEL_MESSAGE));
                Level newLevel = currentLevel.getMode().getLevels()[currentLevel.getLevel()-1+1];
                if (countdownTimer != null){
                    countdownTimer.cancel();
                }
                play(newLevel);

            } else if (success == false){
                AppMessageDialogSingleton dialogSingleton1 = AppMessageDialogSingleton.getSingleton();
                dialogSingleton1.showEnd(propertyManager.getPropertyValue(LOST_LABEL_TITLE), propertyManager.getPropertyValue(LOST_LABEL_MESSAGE ));
                if (countdownTimer != null){
                    countdownTimer.cancel();
                }
                play(currentLevel);
            }
        });

    }

    private void initializeCircles(HBox lettersZone){

    }

    private void initializeLetters(){
        int wordAmount = currentLevel.getWordAmount();
        ArrayList<Integer> has = new ArrayList<>();


        has.add(-1);

        int size = 0; //determine if 16 circles are fully filled.
//        int charIndex1 = new Random().nextInt(textsWords.length);
//        while (has.contains(charIndex1)) {
//            charIndex1 = new Random().nextInt(textsWords.length);
//        }
//        has.add(charIndex1);

        ArrayList<ArrayList<Integer>> errorList = new ArrayList<>();
        errorList.add(new ArrayList<>());
        errorList.add(new ArrayList<>());
        errorList.add(new ArrayList<>());

        for (int i=0; i<1; i++) { //i<wordAmount

            if (has.size() == 17){
                break;
            }
            char[] wordChar = wordSequenceToGuess.get(i).getWordvalue().toCharArray();
            ArrayList<Character> charTemp = new ArrayList<>();

//

            int charIndex1 = new Random().nextInt(textsWords.length);
            while (has.contains(charIndex1)) {
                charIndex1 = new Random().nextInt(textsWords.length);
            }
            has.add(charIndex1);

            int wordLength = 0;

            ArrayList<Integer> initIndexTries = new ArrayList<>();

            textsWords[charIndex1].setText(String.valueOf(wordChar[0]));
            //initIndexTries.add(charIndex1);

            for (int rest=0; rest<16; rest++){
                if (!has.contains(0)) {
                    initIndexTries.add(rest);
                }
            }



            charTemp.add(wordChar[0]);

            wordLength = wordLength+1;

            if (has.size() == 17){
                break;
            }


//            ArrayList<Integer> errorIndexes = new ArrayList<>();
//            errorIndexes.add(-1);
            ArrayList<Integer> initIndexError = new ArrayList<>();
            initIndexError.add(-1);

            errorList.get(i).clear();
            errorList.get(i).add(-1);

            while (wordLength < wordChar.length && has.size() != 17){
                ArrayList<Integer> neighbours = getNeighbour(charIndex1);
                int charIndex2 = new Random().nextInt(neighbours.size());

                int conflictsTest  = 0;
                int errorIndex = -1; // error index for textWords
                int wordLengthZeroCount = 0;

                ArrayList<Integer> tried = new ArrayList<>();

                while (has.contains(neighbours.get(charIndex2))  || errorList.get(i).contains(neighbours.get(charIndex2)) || initIndexError.contains(neighbours.get(charIndex2))) {

                    if (wordLength==0){

                        charIndex1 = new Random().nextInt(textsWords.length);
                        while (has.contains(charIndex1) || initIndexError.contains(charIndex1)) {
                            charIndex1 = new Random().nextInt(textsWords.length);
                        }
                        has.add(charIndex1);
                        textsWords[charIndex1].setText(String.valueOf(wordChar[0]));

                        charTemp.add(wordChar[0]);

                        wordLength = wordLength+1;

                        neighbours = getNeighbour(charIndex1);



                        //break;
                    }

                    if (has.size() == 17){
                        break;
                    }

                    charIndex2 = new Random().nextInt(neighbours.size());
                    //conflictsTest++;
                    tried.add(neighbours.get(charIndex2));
                    if (containsAll(neighbours, tried)){
                        conflictsTest++;
                    }



                    // containALl return true if tried contain all elements of neighbour.
                    if ( containsAll(neighbours, tried) && conflictsTest > 1 ) { //neighbours.size()   // conflictsTest > 10

                        int charTempCurrentSize = charTemp.size();


                        //delete last char.



                        if ( charTemp.size() == 0 && i >=1  ){
                            i--;
                            wordChar = wordSequenceToGuess.get(i).getWordvalue().toCharArray();
                            wordLength = wordChar.length;
                            charTemp = new ArrayList<>();
                            for (int chai=0; chai < wordChar.length; chai++){
                                charTemp.add(wordChar[chai]);
                            }
                            charTempCurrentSize = charTemp.size();

                            initIndexError.clear();
                            initIndexError.add(-1);
                        }




                        int hasIndex = has.size() - 1;
                        int index = has.get(hasIndex);

                        if (wordLength >0) {


                            if (has.size() > 1) {

                                //textWord's index
                                //errorIndexes.add(index);
                                errorList.get(i).add(index);


                                textsWords[index].setText(null);
                                has.remove(hasIndex);
                            }

                            if (charTempCurrentSize > 0) {
                                charTemp.remove(charTempCurrentSize - 1);
                            }


                            // resetting
                            int lastIndex = has.get(has.size() - 1);

                            charIndex1 = lastIndex;
                            neighbours = getNeighbour(charIndex1);
                            charIndex2 = new Random().nextInt(neighbours.size());
//                        while (has.contains(charIndex2) || neighbours.get(charIndex2)==errorIndex) {
//                            charIndex2 = new Random().nextInt(textsWords.length);
//                        }
                            //has.add(charIndex2);
//                        while (has.contains(neighbours.get(charIndex2))){
//                            charIndex2 = new Random().nextInt(neighbours.size());
//                        }


                            wordLength--;
                        }


                        //textsWords[neighbours.get(charIndex2)].setText(String.valueOf(wordChar[0]));
                        //wordLength++;
                        if (wordLength == 0){
//                            errorIndexes.clear();
//                            errorIndexes.add(-1);
                            //errorIndexes.add(index);
                            errorList.get(i).clear();
                            errorList.get(i).add(-1);
                            initIndexError.add(index);
                        }
                        if (wordLength == 1){
                            initIndexError.add(charIndex1);
                        }

                        conflictsTest = 0;
                    }
                }

//                if (charTemp.size() == 0 ){
//                    errorIndexes.clear();
//                    errorIndexes.add(-1);
//                }
                if (wordLength > 0) {
                    has.add(neighbours.get(charIndex2));
                }

                if ( wordLength != wordChar.length && wordLength >= 0) {
                    textsWords[neighbours.get(charIndex2)].setText(String.valueOf(wordChar[wordLength]));

                    charTemp.add(wordChar[wordLength]);
                }

                if ( wordLength == 0){
                    initIndexTries.add(neighbours.get(charIndex2));
                }

                wordLength++;

                charIndex1 = neighbours.get(charIndex2);
            }



        }

        // randomly set rest two words
        for (int i=1; i< wordAmount; i++){
            char[] letters = wordSequenceToGuess.get(i).getWordvalue().toCharArray();

            for (int j=0; j< letters.length; j++) {
                int letterIndex = new Random().nextInt(textsWords.length);
                while (has.contains(letterIndex)) {
                    letterIndex = new Random().nextInt(textsWords.length);
                    if (has.size() == 17){
                        break;
                    }
                }
                if (has.size() < 17) {
                    has.add(letterIndex);
                    textsWords[letterIndex].setText(String.valueOf(letters[j]));
                } else {
                    break;
                }
            }
        }

    }


    private void updateLetters(){
        startingLettersBox.getChildren().clear();

        Text[] texts = new Text[2];
        texts[0] = new Text(String.valueOf(wordSequenceString.get(0).charAt(0)));
        texts[1] = new Text(String.valueOf(wordSequenceString.get(0).charAt(1)));
        startingLettersBox.getChildren().add(texts[0]);
        startingLettersBox.getChildren().add(texts[1]);

        initializeLetters();
    }

    private void updateGuessed(){

        wordBox.getChildren().clear();
        pointsBox.getChildren().clear();

        wordBox.setVisible(true);
        pointsBox.setVisible(true);



        for (int i = 0; i < guessedWord.size(); i++) {
            wordBox.getChildren().add(guessedWord.get(i).getWord());
            pointsBox.getChildren().add(guessedWord.get(i).getPoint());
            //point = point + guessedWord.get(i).getPoints();
        }

        Label total = new Label("Total");
        total.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: white; -fx-opacity: 1; -fx-background-color: rgb(183, 187, 191)");
        Label totalPoints = new Label(String.valueOf(point));
        totalPoints.setStyle("-fx-font-size: 12pt; -fx-font-family: \"Segoe UI Light\"; -fx-text-fill: white; -fx-opacity: 1; -fx-background-color: rgb(183, 187, 191)");

        total.setAlignment(Pos.BOTTOM_CENTER);
        totalPoints.setAlignment(Pos.BOTTOM_CENTER);
        wordBox.getChildren().add(total);
        pointsBox.getChildren().add(totalPoints);

    }


    public void play(Level level) {
        success = false;
        workSpace.reinitializeAfterLevelSelection(level);
        currentLevel = level;
        wordBox = workSpace.getWordBox(); // for storing guessed word.
        pointsBox = workSpace.getPointsBox();
        startingLettersBox = workSpace.getStartingLettersBox();
        targetPoint = workSpace.getTargetPoint();
        remainingTime = workSpace.getRemainingTime();
        guessedWord = workSpace.getGuessedWord();
        guessedWord.clear();
        circles = workSpace.getCircles();
        textsWords = workSpace.getTextsWords();
        wordsSet = workSpace.getWordsSet();
        wordSequenceToGuess = workSpace.getWordSequenceToGuess();
        lines = workSpace.getLines();
        time = level.getRemainingTime();
        initializeLetters();
        point = 0;

        wordSequenceString = new ArrayList<>();
        for (int i=0; i< level.getWordAmount(); i++){
            wordSequenceString.add(wordSequenceToGuess.get(i).getWordvalue());
        }


        if (countdownTimer!=null){
            countdownTimer.cancel();
        }

        countdownTimer = new Timer();
        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {


                //updateTime();
                Platform.runLater(() -> updateTime());


            }
        }, 3000,1000);

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        ArrayList<ArrayList<Integer>> neighboursList = new ArrayList<>();
        ArrayList<ArrayList<Character>>  guessingAttempts = new ArrayList<>();

        int[] count = new int[1];
        count[0] = -1;


        AnimationTimer anitimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                buzzWord.getGUI().getPrimaryScene().setOnKeyTyped((KeyEvent event) -> {

                    char guess = event.getCharacter().charAt(0);

                    if (guess == '\r'){   //event.getCode() == (KeyCode.ENTER)
                        if (guessingAttempts.size() >= 1) {

                            ArrayList<Character> guessing = new ArrayList<>();

                            if (guessingAttempts.size() == 1){
                                guessing = guessingAttempts.get(0);
                            }else {
                                for (int i = 0; i < guessingAttempts.size(); i++) {
                                    for (int j = i + 1; j < guessingAttempts.size(); j++) {
                                        if (guessingAttempts.get(i).size() > guessingAttempts.get(j).size()) {
                                            guessingAttempts.set(j, guessingAttempts.get(i));
                                        } else {
                                            guessingAttempts.set(i, guessingAttempts.get(j));
                                        }
                                    }
                                }
                            }

                            guessing = guessingAttempts.get(0);

                            String guessingTry = "";

                            for (int i=0; i<guessing.size(); i++){
                                guessingTry = guessingTry+guessing.get(i);
                            }

                            for (int i=0; i<circles.length; i++){
                                deHighlight(circles[i]);
                            }

                            paths.clear();
                            neighboursList.clear();
                            guessingAttempts.clear();
                            count[0] = -1;



                            String string = wordSequenceString.get(0);
                            string = string.toLowerCase();

                            if (contains(guessingTry, string)){ //guessingTry.contains(string)

                                guessedWord.add(new Word(string));




                                Word guessedWord = wordSequenceToGuess.get(0);
                                wordSequenceToGuess.remove(0);
                                wordSequenceToGuess.add(guessedWord);

                                wordSequenceString = new ArrayList<>();
                                for (int i=0; i< level.getWordAmount(); i++){
                                    wordSequenceString.add(wordSequenceToGuess.get(i).getWordvalue());
                                }

                                updateLetters();

                                point = point + guessedWord.getPoints();
                                updateGuessed();

                                if (point > level.getTargetPoint()){
                                    success = true;
                                    GameMode gameMode = level.getMode();
                                    gameMode.setLevelPassed(gameMode.getLevelPassed()+1);

                                }else {
                                    success = false;
                                }
                            }

//                            if (time<=0 || success){
//                                stop();
//                            }





                        }

                    }


                    count[0] = count[0]+1;
                    int index1;
                    int index2;




                    if (isValidChar(guess) && !alreadyTried(paths, guess) ){

                        if (count[0] == 0) {
                            ArrayList<Integer> indexes = indexesAtTextWord(guess);

                            for (int i = 0; i < indexes.size(); i++) {
                                hightlight(circles[indexes.get(i)]);
                                //paths.get(i).add(indexes.get(i));

                                ArrayList<Integer> indexI = new ArrayList<>();
                                indexI.add(indexes.get(i));
                                paths.add(indexI);


                                ArrayList<Character> guessTry = new ArrayList<>();
                                guessTry.add(Character.toLowerCase(textsWords[indexes.get(i)].getText().charAt(0)));

                                //guessingWord.add(textsWords[indexes.get(i)].getText().charAt(0))
                                guessingAttempts.add(guessTry);

                                neighboursList.add(getNeighbour(indexes.get(i)));

                            }
                        } else {

                            ArrayList<Integer> indexes = indexesAtTextWord(guess);
                            indexes = excludeFromPath(paths, indexes);
                            for (int i = 0; i < paths.size(); i++) {


                                if (isNotConataining(neighboursList.get(i), indexes) ) {
                                    // return true if neighboursList.get(i) doesn't contain any elements of indexes

                                    for (int j = 0; j < paths.get(i).size(); j++) {
                                        // deHighlight
                                        deHighlight(circles[paths.get(i).get(j)]);
//                                        neighboursList.remove(i);
//                                        guessingAttempts.remove(i);
//                                        paths.remove(i);


//                                        j--;
                                    }

                                    neighboursList.get(i).clear();
                                    guessingAttempts.get(i).clear();
                                    paths.get(i).clear();

//                                    i--; // remove will shift left. i-- will remain correct index after the i++.
                                } else {
                                    // if neighbourList.get(i) contains one or more elements from indexes;

                                    // restrict the index within neighbourList.get(i)
                                    ArrayList<Integer> neighbourTemp = neighboursList.get(i);
                                    int occuranceCount = -1;


                                    for (int indexesIndex = 0; indexesIndex < indexes.size(); indexesIndex++) {
                                        if (neighbourTemp.contains(indexes.get(indexesIndex)) ) {
                                            occuranceCount++;

                                            if (occuranceCount == 0) {

                                                // find where does it occurrence first!!


                                                paths.get(i).add(indexes.get(indexesIndex));
                                                guessingAttempts.get(i).add(Character.toLowerCase(textsWords[indexes.get(indexesIndex)].getText().charAt(0)));
                                                neighboursList.set(i, getNeighbour(indexes.get(indexesIndex)));
                                                hightlight(circles[indexes.get(indexesIndex)]);

                                            } else if (occuranceCount > 1) {
                                                hightlight(circles[indexes.get(i)]);
                                                //paths.get(i).add(indexes.get(i));

                                                ArrayList<Integer> indexI = new ArrayList<>();
                                                indexI.add(indexes.get(i));
                                                paths.add(indexI);


                                                ArrayList<Character> guessTry = new ArrayList<>();
                                                //guessTry.add(textsWords[indexes.get(i)].getText().charAt(0));
                                                guessTry.add(Character.toLowerCase(textsWords[indexes.get(i)].getText().charAt(0)));

                                                //guessingWord.add(textsWords[indexes.get(i)].getText().charAt(0))
                                                guessingAttempts.add(guessTry);

                                                neighboursList.add(getNeighbour(indexes.get(i)));
                                            }


                                        }
                                        occuranceCount = -1;
                                    }


                                }


                                if (areEmptyOrNull(paths, neighboursList, guessingAttempts)) {
                                    count[0] = -1;
                                    //break;
                                }



                            }
                            if (areEmptyOrNull(paths, neighboursList, guessingAttempts)) {
                                count[0] = -1;
                                //break;
                            }
                        }



                    }else {
                        if (!alreadyTried(paths, guess) || !isValidChar(guess)) {
                            for (int i = 0; i < paths.size(); i++) {
                                dehighlightAll();
                                paths.clear();
                                neighboursList.clear();
                                guessingAttempts.clear();
                                count[0] = -1;
                            }
                        }
                    }

                });

                if (time<=0 || success){
                    stop();
                }
            }

            @Override
            public void stop(){
                super.stop();
                end();
            }
        };

        anitimer.start();
    }


    private void dehighlightAll(){
        for (int i=0; i<circles.length; i++){
            deHighlight(circles[i]);
        }

    }

    private boolean contains(String a, String b){
        // see if string a contains all char in b: each char in b can be found in a.
        // a = guess; b = answer
        if (a.length() <  b.length()){
            return false;
        }

        ArrayList<Character> stringA = new ArrayList<>();
        ArrayList<Character> stringB = new ArrayList<>();

        for (int i=0; i<a.length(); i++){
            stringA.add(a.charAt(i));
        }

        for (int i=0; i<b.length(); i++){
            stringB.add(b.charAt(i));
        }

        boolean isContain = true;
        for (int i=0; i<stringB.size(); i++){
            if (!stringA.contains(stringB.get(i))){
                isContain = false;
            }
        }

        return isContain;

    }

    private boolean alreadyTried(ArrayList<ArrayList<Integer>>  path, char c){
//        boolean tried = false;
//        for (int i=0; i<guessingAttempts.size(); i++){
//            if (guessingAttempts.get(i).contains(Character.toLowerCase(c))){
//                tried = true;
//                return tried;
//            }
//        }
//
//        return tried;

        // if the index isnt appear in path, then it is not tried.


        ArrayList<Integer> index = indexesAtTextWord(c);
        boolean notTried = false;

        if (path.size() == 0){
            return false;
        }

        for (int i=0; i<path.size(); i++){
            for (int j=0; j< index.size(); j++){
                if (!path.get(i).contains(index.get(j))){
                    notTried = true;
                }
            }
        }

        if (notTried){
            return false;
        }else {
            return true;
        }



    }

    private ArrayList<Integer> excludeFromPath(ArrayList<ArrayList<Integer>>  path, ArrayList<Integer> indexes){
        for (int i=0; i<path.size(); i++){
            for (int x=0; x<path.get(i).size(); x++){
                for (int j=0; j<indexes.size(); j++){
                    if (path.get(i).get(x) == indexes.get(j)){
                        indexes.remove(j);
                    }

                }
            }
        }

        return indexes;
    }





    private boolean isValidChar(char c){
        boolean isValid = false;
        for (int i=0; i< textsWords.length; i++){
            if (Character.toLowerCase(c) == Character.toLowerCase(textsWords[i].getText().charAt(0))){

                isValid = true;
            }
        }

        return isValid;
    }

    private boolean areEmptyOrNull(ArrayList<ArrayList<Integer>> paths, ArrayList<ArrayList<Integer>> neiList, ArrayList<ArrayList<Character>> guessing){
        if (paths.isEmpty() && neiList.isEmpty() && guessing.isEmpty()){
            return true;
        }

        boolean isEMpty = true;

        for (int i=0; i<paths.size(); i++){
            if (!paths.get(i).isEmpty()){
                isEMpty = false;
                return isEMpty;
            }
        }

        for (int i=0; i<neiList.size(); i++){
            if (!neiList.get(i).isEmpty()){
                isEMpty = false;
                return isEMpty;
            }
        }

        for (int i=0; i<guessing.size(); i++){
            if (!guessing.get(i).isEmpty()){
                isEMpty = false;
                return isEMpty;
            }
        }

        return isEMpty;

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
        Label wrong = new Label("Username and Password don't match.");
        Label userNotFound = new Label("User not found");
        userNotFound.setVisible(false);
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
        VBox vBox = new VBox(pane, wrong, userNotFound);
        vBox.setAlignment(Pos.CENTER);



//        String name = username.getText();
//        String pw = passwordField.getText();

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

        Text name = new Text(username.getText());
        Text pw = new Text(passwordField.getText());


//        Dialog dialog = new Dialog();
//        dialog.getDialogPane().getChildren().setAll(dia);
//        dialog.initOwner(buzzWord.getGUI().getWindow());
//        dialog.initModality(Modality.WINDOW_MODAL);
//        dialog.showAndWait();
        cancel.setOnAction(event -> {
            stage.close();
        });



        login.setOnAction(event -> {
            name.setText(username.getText());
            pw.setText(passwordField.getText());
            if (name.getText().length()==0 || pw.getText().length()==0){
                wrong.setVisible(true);
            }else {
                /* load codes here  or create a new method to load and call that here.*/

                User loaded = null;
                try {
                    loaded = loadUser(name.getText());
                } catch (FileNotFoundException e) {
                    wrong.setVisible(false);
                    userNotFound.setVisible(true);

                }
                boolean isPWCorrect = false;

                if (loaded != null) {
                    try {
                        isPWCorrect = loaded.authenticate(pw.getText(), loaded.getPassword(), loaded.getSalt());
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (NullPointerException nu) {

                    }

                    if (isPWCorrect) {
                        stage.close();
                        resetData(loaded);
                        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
                        workSpace.reinitializeAfterLogin(user);
                    } else {
                        wrong.setVisible(true);
                    }
                }


            }

        });



    }

    @Override
    public void handleExitRequest() {
        handlePauseRequest();


        //stage.close();

        boolean leave = false;
        int test = prompToSave();
        int result = selection;

        //result = prompToSave();
        if (result == 1){
            save();
            leave = true;
        }
        if (result == 0){
            leave = false;

        }
        if (result == 2){
            leave = false;

        }

        if (leave)
            System.exit(0);
    }

    public void handleReturnHomeReuqest() {
        WorkSpace workspace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workspace.reinitializeToHome();
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
                buzzWord.setDataComponent(user);
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
        save();
        WorkSpace workspace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workspace.reinitializeAfterLogout();
    }


    public void handleMode0(String mode){
        //GameMode gameMode = new GameMode(mode, gameData);
        //gameData.getModes().set(0, gameMode);

        GameMode gameMode = gameData.getModes().get(0);

        String modename = gameMode.getModeName();

        if (modename != null) {
            gameData.setMode(0, gameMode);
        }else {
            gameMode = new GameMode(mode, gameData);
            gameData.getModes().set(0, gameMode);
        }
        //gameData.getModes()[0] = gameMode;
        workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);

        //play();

    }

    public void handleMode1(String mode){
        //GameMode gameMode = new GameMode(mode, gameData);
        //gameData.getModes().set(1, gameMode);
        //gameData.getModes()[1] = gameMode;


        GameMode gameMode = gameData.getModes().get(1);
        String modename = gameMode.getModeName();

        if (modename != null) {
            gameData.setMode(1, gameMode);
        }else {
            //gameData.setMode(1, new GameMode(mode, gameData));
            gameMode = new GameMode(mode, gameData);
            gameData.getModes().set(1, gameMode);
        }
        gameData.setMode(1, gameMode);
        workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);
        //play();

    }

    public void handleMode2(String mode){
        //GameMode gameMode = new GameMode(mode, gameData);
        //gameData.getModes().set(2, gameMode);
        //gameData.getModes()[2] = gameMode;

        GameMode gameMode = gameData.getModes().get(2);

        String modename = gameMode.getModeName();

        if (modename != null) {
            gameData.setMode(2, gameMode);
        }else {
            //gameData.setMode(2, new GameMode(mode, gameData));
            gameMode = new GameMode(mode, gameData);
            gameData.getModes().set(2, gameMode);
        }
        gameData.setMode(2, gameMode);
        workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);

        //play();
    }

    public void handleMode3(String mode){
        //GameMode gameMode = new GameMode(mode, gameData);
        //gameData.getModes().set(3, gameMode);
        //gameData.getModes()[3] = gameMode;

        GameMode gameMode = gameData.getModes().get(3);

        String modename = gameMode.getModeName();

        if (modename != null) {
            gameData.setMode(3, gameMode);
        }else {
            //gameData.setMode(3, new GameMode(mode, gameData));
            gameMode = new GameMode(mode, gameData);
            gameData.getModes().set(3, gameMode);
        }

        gameData.setMode(3, gameMode);
        workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        workSpace.reinitializeAfterModeSelection(gameMode);

        //play();
    }



    private int prompToSave(){


        StackPane askuser = new StackPane();






        Label  messageLabel = new Label("Do you want to exit?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(yesButton);
        buttonBox.getChildren().add(noButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(messageLabel, buttonBox);
        vBox.setAlignment(Pos.CENTER);



//        String name = username.getText();
//        String pw = passwordField.getText();

        askuser.getChildren().setAll(vBox);
        Scene scene = new Scene(askuser, 300,200, Color.LIGHTGRAY);
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
        selection = -1;
        noButton.setOnAction(event -> {
            stage.close();
            selection = 0;

        });

        yesButton.setOnAction(event -> {
            stage.close();
            selection = 1;
        });

        //stage.close();
        stage.showAndWait();
        //stage.close();
        //return  selection[0];
        return selection;


    }



    private void save(){
        try {
            FileChooser fc = new FileChooser();
            URL workDirtURL = BuzzWord.class.getClassLoader().getResource(APP_WORKDIR_PATH.getParameter());
            File initialDir = new File(workDirtURL.getFile());
            fc.setInitialDirectory(initialDir);
            String username = user.getUserName()+".json";
            String selectedFile = fc.getInitialDirectory()+"/"+username;
            buzzWord.getFileComponent().saveData(user, Paths.get(selectedFile));

        } catch (Exception e){
            AppMessageDialogSingleton dialogSingleton = AppMessageDialogSingleton.getSingleton();
            dialogSingleton.show(propertyManager.getPropertyValue(SAVE_ERROR_TITLE), propertyManager.getPropertyValue(SAVE_ERROR_MESSAGE));
        }


    }

    private User loadUser(String username) throws FileNotFoundException {
        User loadedUser = null;
        try {
            FileChooser fc = new FileChooser();
            URL workDirtURL = BuzzWord.class.getClassLoader().getResource(APP_WORKDIR_PATH.getParameter());
            File initialDir = new File(workDirtURL.getFile());
            fc.setInitialDirectory(initialDir);
            //String username = user.getUserName()+".json";
            String selectedFile = fc.getInitialDirectory()+"/"+username+".json";
            //buzzWord.getFileComponent().saveData(user, Paths.get(selectedFile));
            loadedUser = (User) buzzWord.getFileComponent().loadData(user,Paths.get(selectedFile));

            //resetData(loadedUser);
        } catch (FileNotFoundException f){
            throw f;
        } catch (Exception e){
            AppMessageDialogSingleton dialogSingleton = AppMessageDialogSingleton.getSingleton();
            dialogSingleton.show(propertyManager.getPropertyValue(LOAD_ERROR_TITLE), propertyManager.getPropertyValue(LOAD_ERROR_MESSAGE));

        }

        return loadedUser;
    }

    private void resetData(User data){
        this.user = data;
        this.gameData = user.getGameData();
        //this.user.setGameData();
        this.gameData.setBuzzWord(buzzWord);
        ArrayList<GameMode>  modes = gameData.getModes();

        for (int i=0; i<modes.size(); i++){
            modes.get(i).setData(gameData);
//            Level[] levels = modes.get(i).getLevels();
//            for (int j =0; j<levels.length; j++){
//
//                levels[j].setMode(modes.get(i));
//            }
            if (modes.get(i).getModeName() != null) {
                modes.get(i).resetLevelsAndWords();
            }
        }




        buzzWord.setDataComponent(user);
    }


    public void setBuzzWord(BuzzWord buzzword){
        this.buzzWord = buzzword;
    }


    public void handlePauseRequest() {
        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        Text[] text = workSpace.getTextsWords();
        for (int i=0; i<text.length; i++){
            text[i].setVisible(false);
        }
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }

        pauseButton.setVisible(false);
        resumeButton.setVisible(true);
    }

    public void handleResumeRequest() {
        WorkSpace workSpace = (WorkSpace) buzzWord.getWorkspaceComponent();
        Text[] text = workSpace.getTextsWords();
        for (int i=0; i<text.length; i++){
            text[i].setVisible(true);
        }
        countdownTimer = new Timer();

        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateTime());
            }
        }, 1000, 1000);

        resumeButton.setVisible(false);
        pauseButton.setVisible(true);
    }


    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    //
    private ArrayList<Integer> getNeighbour(int base){
        ArrayList<Integer> neighbours = new ArrayList<>();

        if (base == 3){
            neighbours.add(2);
            neighbours.add(7);
        }else if (base == 7){
            neighbours.add(3);
            neighbours.add(6);
            neighbours.add(11);
        }else if (base == 11){
            neighbours.add(7);
            neighbours.add(10);
            neighbours.add(15);
        }else if (base == 15){
            neighbours.add(11);
            neighbours.add(14);
        }else if (base == 0){
            neighbours.add(1);
            neighbours.add(4);
        }else if (base == 4){
            neighbours.add(0);
            neighbours.add(5);
            neighbours.add(8);
        }else if (base == 8){
            neighbours.add(4);
            neighbours.add(9);
            neighbours.add(12);
        }else if (base == 12){
            neighbours.add(8);
            neighbours.add(13);
        }else {
            int top = base-4;
            int right = base+1;
            int bottom = base+4;
            int left = base -1;
            if (top>=0 && top <=15){
                neighbours.add(top);
            }
            if (right>=0 && right <= 15){
                neighbours.add(right);
            }
            if (bottom>=0 && bottom <=15){
                neighbours.add(bottom);
            }
            if (left>=0 && left <=15){
                neighbours.add(left);
            }
        }


        return neighbours;
    }



    // test if tried contain all elements in neighbour.
    // return true is containsALL
    private boolean containsAll(ArrayList<Integer> neighbour, ArrayList<Integer> tried){
        int count = 0;

        for (int n=0; n<neighbour.size(); n++){
            if (tried.contains(neighbour.get(n))){
                count++;
            }
        }

        return count == neighbour.size();

    }

    private boolean hasCommonChar(ArrayList<Integer> neighbour){
        for (int i=0; i<neighbour.size(); i++){


            for (int j=i+1; j<neighbour.size(); j++) {
                char first = textsWords[neighbour.get(i)].getText().charAt(0);
                char second = textsWords[neighbour.get(j)].getText().charAt(0);

                if (Character.toLowerCase(first) == Character.toLowerCase(second)){
                    return true;
                }

            }

        }

        return false;
    }


    private void updateTime(){
        if (time>0) {
            time--;
            remainingTime.setText("TimeRemaining: " + time + " seconds");
        }
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

    private ArrayList<Integer> indexesAtTextWord(char c){
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i=0; i<textsWords.length; i++){
            if (Character.toLowerCase(c) == Character.toLowerCase(textsWords[i].getText().charAt(0))){
                indexes.add(i);
            }
        }

        return indexes;
    }

    private boolean isNotConataining(ArrayList<Integer> neighbour, ArrayList<Integer> indexes){
        // if neighbour contain's none elements of indexes; return true
        boolean isNot = true;

        for (int i=0; i<indexes.size(); i++){
            if (neighbour.contains(indexes.get(i))){
                isNot = false;
            }
        }

        return isNot;
    }

    public void handleQuickGameReuqest() {
        PropertyManager propertyManager = PropertyManager.getManager();

        GameMode gameMode = new GameMode("PlaceD", gameData );
        workSpace.reinitializeAfterModeSelection(gameMode);
        play(gameMode.getLevels()[0]);


    }

    public void setWorkSpace(WorkSpace workSpace) {
        this.workSpace = workSpace;
    }
}

