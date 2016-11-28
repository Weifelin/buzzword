package data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import components.AppDataComponent;
import components.AppFileComponent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Weifeng Lin on 11/13/16.
 * @author Weifeng Lin
 */

public class GameDataFile implements AppFileComponent {

    @Override
    public void saveData(AppDataComponent data, Path to) throws IOException {
        ObjectMapper toSave = new ObjectMapper();
        //toSave.writerWithDefaultPrettyPrinter();
        try {

            toSave.writeValue(new File(String.valueOf(to)),data);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void saveUser(User user, Path to) throws IOException{
//        ObjectMapper toSave = new ObjectMapper();
//        toSave.writerWithDefaultPrettyPrinter();
//        try {
//
//            toSave.writeValue(new File(String.valueOf(to)),user);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public AppDataComponent loadData(AppDataComponent data, Path from) {
        ObjectMapper toLoad = new ObjectMapper();
        toLoad.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);

        User file = null;
        try {
            file = toLoad.readValue(new File(String.valueOf(from)), User.class);
        } catch (Exception e){
            e.printStackTrace();
        }



        return file;

    }

    @Override
    public void exportData(AppDataComponent data, Path filePath) throws IOException {

    }
}
