package org.mrdarkimc.DataFetcher.filemanager;

import org.mrdarkimc.DataFetcher.data.DataPacket;
import org.mrdarkimc.DataFetcher.exceptions.ZipWriteException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileManager {
    private Path saveDir = Paths.get("data");
    private Path tempDir = Paths.get("data/temp");
    private final DataSaver dataSaver = new DataSaver(tempDir);
    private final ZipFileGenerator zipFileGenerator = new ZipFileGenerator(saveDir, tempDir);

    public FileManager() {
    }

    public void saveData(DataPacket data) throws IOException, ZipWriteException {
        System.out.println("Начинаю процесс сохранения файлов...");
        createFolder(saveDir);
        createFolder(tempDir);
        dataSaver.saveDataToFile(data);
        System.out.println("Сохранено!");
        System.out.println("Начинаю упаковку в ZIP архив..");
        zipFileGenerator.packFiles(data);
        removeFolder(tempDir);
        System.out.println("Данные упакованы в ZIP архив.");
    }

    private void createFolder(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    private void removeFolder(Path path) {
        try (Stream<Path> pathStream = Files.list(path)) {
            pathStream.map(Path::toFile).forEach(File::delete);
            Files.deleteIfExists(path);

        } catch (IOException e) {
            System.out.println("Ошибка удаления временной папки");
            throw new RuntimeException(e);
        }
    }
}
