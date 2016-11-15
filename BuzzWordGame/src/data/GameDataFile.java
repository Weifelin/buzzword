package data;

import components.AppDataComponent;
import components.AppFileComponent;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */

public class GameDataFile implements AppFileComponent {

    @Override
    public void saveData(AppDataComponent data, Path filePath) throws IOException {

    }

    @Override
    public AppDataComponent loadData(AppDataComponent data, Path filePath) throws IOException {
        return null;
    }

    @Override
    public void exportData(AppDataComponent data, Path filePath) throws IOException {

    }
}
