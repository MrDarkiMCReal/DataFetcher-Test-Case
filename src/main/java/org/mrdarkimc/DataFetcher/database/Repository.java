package org.mrdarkimc.DataFetcher.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Repository {
    public static final String schemaName = "data_fetcher"; //todo hardcode
    public static final String tableName = "denormalized_data"; //todo hardcode
    private DataBaseConnection connection;

    public Repository(DataBaseConnection connection) {
        this.connection = connection;
    }

    public String prepareGenerateTable(List<List<Map<String, String>>> denormalize) {
        Set<String> set = adaptListToKeyset(denormalize);
        return prepareCreateSchamaAndTableUsingKeySet(set);
    }

    //генерит набор ключей JSON файла исходя из всех страниц
    private Set<String> adaptListToKeyset(List<List<Map<String, String>>> denormalize) {
        Set<String> keySet = new LinkedHashSet<>(); //набор ключей со всех страниц со всех энтити

        for (List<Map<String, String>> maps : denormalize) {
            for (Map<String, String> map : maps) {
                Set<String> set = map.keySet(); //набор ключей из одного энтити
                keySet.addAll(set);
            }
        }
        return keySet;
    }

    public String prepareInsertData(List<List<Map<String, String>>> denormalize) {
        Set<String> jsonKeySet = adaptListToKeyset(denormalize);
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(schemaName).append(".").append(tableName).append(" (");

        int count = 0;
        for (String column : jsonKeySet) {
            if (count > 0) {
                sql.append(", ");
            }
            sql.append("\"").append(column).append("\"");
            count++;
        }

        sql.append(") VALUES ");

        boolean firstRow = true;

        for (List<Map<String, String>> maps : denormalize) { //по всем страницам
            for (Map<String, String> map : maps) { //итерация по одной странице
                if (!firstRow) {
                    sql.append(", ");
                }

                StringBuilder valueListBuilder = new StringBuilder("(");

                int valueCount = 0;
                for (String column : jsonKeySet) {
                    if (valueCount > 0) {
                        valueListBuilder.append(", ");
                    }

                    String value = map.getOrDefault(column, ""); //в апи поля могут быть пустые
                    if (column.equals("id")) {
                        valueListBuilder.append(value);
                    } else {
                        valueListBuilder.append("'").append(value.replace("'", "''")).append("'");
                    }

                    valueCount++;
                }
                valueListBuilder.append(")");
                sql.append(valueListBuilder);

                firstRow = false;
            }
        }
        sql.append(buildConflict(jsonKeySet));
        sql.append(";");
        return sql.toString();
    }

    public String buildConflict(Set<String> keyset) { //todo hardcode
        StringBuilder conflictBuilder = new StringBuilder();
        conflictBuilder.append("ON CONFLICT (\"id\") DO UPDATE SET ");
        boolean firstColumn = true;

        for (String column : keyset) {
            if (column.equals("id")) {
                continue;
            }

            if (!firstColumn) {
                conflictBuilder.append(", ");
            }

            firstColumn = false;
            conflictBuilder.append("\"").append(column).append("\"").append(" = EXCLUDED.\"").append(column).append("\"");
        }
        return conflictBuilder.toString();
    }

    public String prepareCreateSchamaAndTableUsingKeySet(Set<String> keyset) {

        StringBuilder sql = new StringBuilder("CREATE SCHEMA IF NOT EXISTS " + schemaName + "; ");
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(schemaName).append(".").append(tableName).append(" (");

        int count = 0;
        for (String column : keyset) {
            if (count > 0) {
                sql.append(", ");
            }
            if (column.equals("id")) {
                sql.append("\"").append(column).append("\" INTEGER PRIMARY KEY");
            } else {
                sql.append("\"").append(column).append("\" TEXT");
            }

            count++;
        }

        sql.append(");");
        return sql.toString();
    }

    public void execute(String cmd) throws SQLException {
        try (Connection connection = this.connection.openConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(cmd);
        }
    }
}
