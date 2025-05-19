package org.mrdarkimc.DataFetcher;

import org.mrdarkimc.DataFetcher.Logger.Logger;
import org.mrdarkimc.DataFetcher.attributes.Attribute;
import org.mrdarkimc.DataFetcher.attributes.AttributeUtils;
import org.mrdarkimc.DataFetcher.data.DataFetcher;
import org.mrdarkimc.DataFetcher.data.DataPacket;
import org.mrdarkimc.DataFetcher.data.Denormalizer;
import org.mrdarkimc.DataFetcher.exceptions.UserDeclinedException;
import org.mrdarkimc.DataFetcher.database.DataBaseConnection;
import org.mrdarkimc.DataFetcher.database.Repository;
import org.mrdarkimc.DataFetcher.exceptions.NoRecordsFoundException;
import org.mrdarkimc.DataFetcher.exceptions.WrongPageSizeParam;
import org.mrdarkimc.DataFetcher.exceptions.ZipWriteException;
import org.mrdarkimc.DataFetcher.filemanager.FileManager;
import org.mrdarkimc.DataFetcher.link.Link;
import org.mrdarkimc.DataFetcher.listeners.UserInputListener;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static String name = "7710568760-RUBPNUBP";
    private static DataBaseConnection connection;
    public static UserInputListener listener = new UserInputListener();

    static {
        initDB();
    }

    public static DataBaseConnection initDB() {
        String host = System.getProperty("DB_HOST");
        String port = System.getProperty("DB_PORT");
        String db_name = System.getProperty("DB_NAME");
        String user = System.getProperty("DB_USER");
        String pass = System.getProperty("DB_PASSWORD");
        connection = new DataBaseConnection(host, port, db_name, user, pass);
        return connection;
    }


    public static void main(String[] args) {
        boolean run = true;
        while (run) {
            Logger.showHint();

            String userInput = listener.askInput();
            try {
                Validator.isCorrectInput(userInput.split(" "));
            } catch (IllegalArgumentException e) {
                continue;
            }

            Attribute[] attributesFromString = AttributeUtils.getAttributesFromString(userInput);

            Attribute from = AttributeUtils.tryFindFromKey("filterminloaddate", attributesFromString); //минимальная дата загрузки, которая будет учавствовать в фильре
            Attribute to = AttributeUtils.tryFindFromKey("filtermaxloaddate", attributesFromString); //максимальная дата загрузки, которая будет учавствовать в фильре

            Attribute[] extraAttributes = Arrays.stream(attributesFromString)
                    .filter(e -> !e.getKey().equals("filterminloaddate"))
                    .filter(e -> !e.getKey().equals("filtermaxloaddate"))
                    .toArray(Attribute[]::new);
            try {
                if (from != null) Validator.validateDateFormat(from.getValue());
                if (to != null) Validator.validateDateFormat(to.getValue());
            } catch (IllegalArgumentException e) {
                continue;
            }

            DataPacket fetchedData = null;
            try {
                fetchedData = getDataPacket(from, to, extraAttributes);
            } catch (UserDeclinedException e) {
                System.out.println("Возвращаемся к началу программы");
                continue;
            } catch (IOException e) {
                System.out.println("Не удалось сохранить данные в папку data");
                System.out.println("Возвращаемся к началу программы");
                continue;
            } catch (NoRecordsFoundException e) {
                System.out.println("Не найдено записей по текущему запросу");
                System.out.println("Возвращаемся к началу программы");
                continue;
            } catch (WrongPageSizeParam e) {
                System.out.println("Возвращаемся к началу программы");
            }
            if (!saveData(fetchedData)) {
                continue;
            }

            Denormalizer denormalizer = new Denormalizer(fetchedData);
            try {
                //иерархия списка: Список из страниц(размер этого списка зависит от кол-ва загруженных страниц). В каждой странице хранится список с энтити(1 элемент из массива data).
                //Энтити представлена в виде пары ключ - значение, где ключ - наименование поля из JSON а значение - значение для энтити
                //вложеные JSON обьекты в виде массива сохраняются в виде json массива
                List<List<Map<String, String>>> denormalize = denormalizer.denormalize();
                Repository repository = new Repository(connection);

                String tableGenerationSQL = repository.prepareGenerateTable(denormalize);
                String insertionSQL = repository.prepareInsertData(denormalize);

                repository.execute(tableGenerationSQL);
                repository.execute(insertionSQL);

                System.out.println("Данные сохранены в БД");
            } catch (SQLException e) {
                System.out.println("Ошибка записи данных в БД. Пожалуйста, перепроверьте данные имя пользователя и пароля. текущие значения:");
                connection.showAbout();
                System.out.println("Убедитесь, что база данных запущена. Если вы используете Docker, то запустите сначала run-Docker");
            }
            if (System.getProperty("DontAsk") != null || listener.askYesOrNo("Хотите загрузить еще данные?")) {
                continue;
            } else {
                run = false;
            }
        }
    }

    private static DataPacket getDataPacket(Attribute from, Attribute to, Attribute[] extra) throws UserDeclinedException, IOException, NoRecordsFoundException, WrongPageSizeParam {
        Link link = new Link(from, to, extra);
        DataFetcher fetcher = new DataFetcher();
        DataPacket fetchedData = fetcher.fetch(link);
        return fetchedData;
    }

    public static boolean saveData(DataPacket data) {
        FileManager manager = new FileManager();
        try {
            manager.saveData(data);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить данные.");
            System.out.println(e.getMessage());
            return false;
        } catch (ZipWriteException e) {
            System.out.println("Не удалось создать ZIP файл. ");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}