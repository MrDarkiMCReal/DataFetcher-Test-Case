package org.mrdarkimc.DataFetcher.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

public class RepositoryTest {

    @Test
    public void testSchemaAndTableGeneration() throws SQLException {
        String schemaAndTable = Repository.schemaName + "." + Repository.tableName;

        Repository repository = new Repository(new DataBaseConnection("", "", "", "", ""));
        String premade = "CREATE SCHEMA IF NOT EXISTS " + Repository.schemaName+ "; CREATE TABLE IF NOT EXISTS "+ schemaAndTable +" (\"id\" INTEGER PRIMARY KEY, \"info_regNum\" TEXT, \"info_code\" TEXT, \"activities\" TEXT);";
        String autogen = repository.prepareGenerateTable(getExampleDenormalizedData());
        Assertions.assertEquals(premade,autogen);
    }

    @Test
     public void testInsertValueGeneration() {
        String schemaAndTable = Repository.schemaName + "." + Repository.tableName;
        Repository repository = new Repository(new DataBaseConnection("", "", "", "", ""));

        String autogen = repository.prepareInsertData(getExampleDenormalizedData());

        String prepared = "INSERT INTO " + schemaAndTable + " (\"id\", \"info_regNum\", \"info_code\", \"activities\") VALUES (4556262, 'Ъ3864', '400Ъ3864', '[{\"activityCode\":\"41.20\",\"activityName\":\"Строительство жилых и нежилых зданий\",\"activityKind\":\"основной\"}]')ON CONFLICT (\"id\") DO UPDATE SET \"info_regNum\" = EXCLUDED.\"info_regNum\", \"info_code\" = EXCLUDED.\"info_code\", \"activities\" = EXCLUDED.\"activities\";";
        System.out.println(prepared);
        Assertions.assertEquals(prepared,autogen);
    }
    public List<List<Map<String, String>>> getExampleDenormalizedData(){
        List<List<Map<String, String>>> pages = new ArrayList<>();
        List<Map<String, String>> entities = new ArrayList<>();

        Map<String, String> entity1 = new LinkedHashMap<>();
        entity1.put("id", "4556262");
        entity1.put("info_regNum", "Ъ3864");
        entity1.put("info_code", "400Ъ3864");
        entity1.put("activities", "[{\"activityCode\":\"41.20\",\"activityName\":\"Строительство жилых и нежилых зданий\",\"activityKind\":\"основной\"}]");
        entities.add(entity1);
        pages.add(entities);
        return pages;
    }
}
