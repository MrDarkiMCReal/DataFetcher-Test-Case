package org.mrdarkimc.DataFetcher.filemanager;

import com.fasterxml.jackson.databind.JsonNode;
import org.mrdarkimc.DataFetcher.data.DataPacket;
import org.mrdarkimc.DataFetcher.data.DataTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DataSaver {

    private final Path tempDir;

    public DataSaver(Path tempDir) {
        this.tempDir = tempDir;
    }

    public void saveDataToFile(DataPacket data, DataTypes... datatype) { //json xml
        if (datatype.length >0) {
            for (DataTypes dataType : datatype) {
                saveDataByType(data, dataType);
            }
            return;
        }
        saveDataByType(data, DataTypes.JSON);
        saveDataByType(data, DataTypes.XML);

    }


    private void saveDataByType(DataPacket data, DataTypes type) {
        List<JsonNode> list = data.getList();

        String fileName = FileNamingStrategy.getNamingForDataFile(data, type.getFormat());
        for (int i = 0; i < list.size(); i++) {

            File file = tempDir.resolve(String.format(fileName, i + 1)).toFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                type.getMapper().writerWithDefaultPrettyPrinter().writeValue(writer, list.get(i));
            } catch (IOException e) {
                System.out.println("Ошибка сохранения данных");
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e); //refactor this
            }
        }
    }

}
