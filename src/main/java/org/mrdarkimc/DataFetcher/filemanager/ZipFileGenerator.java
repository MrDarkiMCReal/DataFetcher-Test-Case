package org.mrdarkimc.DataFetcher.filemanager;

import org.mrdarkimc.DataFetcher.data.DataPacket;
import org.mrdarkimc.DataFetcher.exceptions.ZipWriteException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.util.List;

import java.util.zip.ZipEntry;

import java.util.zip.ZipOutputStream;

public class ZipFileGenerator {
    private Path saveDir;
    private Path tempDir;

    public ZipFileGenerator(Path saveDir, Path tempPath) {
        this.saveDir = saveDir;
        this.tempDir = tempPath;
    }

    public void packFiles(DataPacket data) throws ZipWriteException, IOException {
        String fileName = FileNamingStrategy.getNamingForZipFile(data);
        Path file = saveDir.resolve(fileName);
        packFiles(file);
    }

    private void packFiles(Path zipFile) throws ZipWriteException, IOException {
        List<ZipWriteException> exceptions = new ArrayList<>();

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile.toFile()))) {
            Files.list(tempDir)
                    .filter(path -> path.toString().endsWith(".json") || path.toString().endsWith(".xml"))
                    .forEach(path -> {
                        try (InputStream is = Files.newInputStream(path)) {
                            ZipEntry entry = new ZipEntry(path.getFileName().toString());
                            zos.putNextEntry(entry);

                            byte[] buffer = new byte[2024];
                            int len;
                            while ((len = is.read(buffer)) > 0) {
                                zos.write(buffer, 0, len);
                            }
                            zos.closeEntry();
                        } catch (IOException e) {
                            exceptions.add(new ZipWriteException("Ошибка добавления файла в ZIP: " + path.getFileName()));
                        }
                    });
        }
        for (ZipWriteException exception : exceptions) {
            throw exception;
        }
    }
}
