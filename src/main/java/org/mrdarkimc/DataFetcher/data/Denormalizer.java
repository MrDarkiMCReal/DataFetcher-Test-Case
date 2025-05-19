package org.mrdarkimc.DataFetcher.data;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class Denormalizer {
    private final DataPacket data;

    public Denormalizer(DataPacket data) {
        this.data = data;
    }

    public List<List<Map<String, String>>> denormalize() throws IllegalArgumentException {
        List<JsonNode> pages = data.getList();

        List<List<Map<String, String>>> finalResult = new ArrayList<>();

        for (JsonNode jsonNode : pages) {
            List<Map<String, String>> maps = denormalizePageData(jsonNode);
            finalResult.add(maps);
        }
        return finalResult;
    }

    public List<Map<String, String>> denormalizePageData(JsonNode rootNode) {

        List<Map<String, String>> result = new ArrayList<>();

        JsonNode dataArray = rootNode.get("data");
        if (dataArray == null || !dataArray.isArray()) {
            throw new IllegalArgumentException("Ошибка чтения данных. Payload отсутствует.");//todo проверить, если всего 1 элмент в records то что отдает апи, массив или обьект
        }

        for (JsonNode element : dataArray) {
            Map<String, String> flatMap = new LinkedHashMap<>();
            defineNextStep("", element, flatMap);
            result.add(flatMap);
        }

        return result;
    }

    private void defineNextStep(String prefix, JsonNode node, Map<String, String> map) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String newPrefix = prefix.isEmpty() ? entry.getKey() : prefix + "_" + entry.getKey();
                defineNextStep(newPrefix, entry.getValue(), map);
            });
        } else if (node.isArray()) {
            map.put(prefix, node.toString());
        } else {
            map.put(prefix, node.asText());
        }
    }
}
