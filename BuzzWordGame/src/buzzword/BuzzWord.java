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

/**
 * Created by Red on 11/13/16.
 */
public class BuzzWord extends AppTemplate {

    BuzzWordGUI gui;





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


    public static void main(String[] args) {
        launch(args);
    }
}
