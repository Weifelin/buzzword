package buzzword;

import apptemplate.AppTemplate;
import components.AppComponentsBuilder;
import components.AppDataComponent;
import components.AppFileComponent;
import components.AppWorkspaceComponent;
import data.GameData;
import data.GameDataFile;
import gui.BuzzWordGUI;
import gui.WorkSpace;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.AppMessageDialogSingleton;
import ui.YesNoCancelDialogSingleton;

import java.io.File;
import java.net.URL;

import static settings.AppPropertyType.*;
import static settings.AppPropertyType.PROPERTIES_LOAD_ERROR_MESSAGE;
import static settings.InitializationParameters.APP_PROPERTIES_XML;
import static settings.InitializationParameters.WORKSPACE_PROPERTIES_XML;

/**
 * Created by Red on 11/13/16.
 */
public class BuzzWord extends AppTemplate {

    BuzzWordGUI gui;
    GameDataFile gamedataFile =  new GameDataFile();




    @Override
    public BuzzWordGUI getGUI() {
        return gui;
    }

    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
        return new AppComponentsBuilder() {
            @Override
            public AppDataComponent buildDataComponent() throws Exception {
                return new GameData(BuzzWord.this);
            }

            @Override
            public AppFileComponent buildFileComponent() throws Exception {
                return new GameDataFile();
            }

            @Override
            public AppWorkspaceComponent buildWorkspaceComponent() throws Exception {
                return new WorkSpace(BuzzWord.this);
            }
        };
    }

    public String getFileControllerClass() {
        return "BuzzWordController";
    }



    @Override
    public void start(Stage primaryStage) {
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        YesNoCancelDialogSingleton yesNoDialog   = YesNoCancelDialogSingleton.getSingleton();
        messageDialog.init(primaryStage);
        yesNoDialog.init(primaryStage);

        try {
            if (loadProperties(APP_PROPERTIES_XML) && loadProperties(WORKSPACE_PROPERTIES_XML)) {
                AppComponentsBuilder builder = makeAppBuilderHook();

                fileComponent = builder.buildFileComponent();
                dataComponent = builder.buildDataComponent();
                gui = (propertyManager.hasProperty(APP_WINDOW_WIDTH) && propertyManager.hasProperty(APP_WINDOW_HEIGHT))
                        ? new BuzzWordGUI(primaryStage, propertyManager.getPropertyValue(APP_TITLE.toString()), this,
                        Integer.parseInt(propertyManager.getPropertyValue(APP_WINDOW_WIDTH)),
                        Integer.parseInt(propertyManager.getPropertyValue(APP_WINDOW_HEIGHT)))
                        : new BuzzWordGUI(primaryStage, propertyManager.getPropertyValue(APP_TITLE.toString()), this);
                workspaceComponent = builder.buildWorkspaceComponent();
                initStylesheet();
                //initStylesheet(primaryStage);
                gui.initStyle();
                workspaceComponent.initStyle();
            }
        } catch (Exception e) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(propertyManager.getPropertyValue(PROPERTIES_LOAD_ERROR_TITLE.toString()),
                    propertyManager.getPropertyValue(PROPERTIES_LOAD_ERROR_MESSAGE.toString()));
        }
    }

//    private void initStylesheet(Stage primaryStage) {
//        primaryStage.setResizable(true);
//        primaryStage.setWidth(1000);
//        primaryStage.setHeight(720);
//        primaryStage.setMinHeight(720);
//        primaryStage.setMinWidth(1000);
//    }

    @Override
    public void initStylesheet() {
        URL cssResource = getClass().getClassLoader().getResource(propertyManager.getPropertyValue(APP_PATH_CSS) +
                File.separator +
                propertyManager.getPropertyValue(APP_CSS));
        assert cssResource != null;
        gui.getPrimaryScene().getStylesheets().add(cssResource.toExternalForm());
    }


    public static void main(String[] args) {
        launch(args);
    }


}
