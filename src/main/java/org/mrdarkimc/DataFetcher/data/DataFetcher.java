package org.mrdarkimc.DataFetcher.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mrdarkimc.DataFetcher.Main;
import org.mrdarkimc.DataFetcher.attributes.Attribute;
import org.mrdarkimc.DataFetcher.exceptions.NoRecordsFoundException;
import org.mrdarkimc.DataFetcher.exceptions.UserDeclinedException;
import org.mrdarkimc.DataFetcher.exceptions.WrongPageSizeParam;
import org.mrdarkimc.DataFetcher.link.Link;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataFetcher {
    private final List<JsonNode> pagesAsJson = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private long delay = 1500; //todo hardcode

    public DataPacket fetch(Link link) throws IOException, NoRecordsFoundException, WrongPageSizeParam, UserDeclinedException {

        Link.LinkBuilder builder = link.newBuilder();
        System.out.println("Начинаю скачивать 1ю страницу...");
        JsonNode node = mapper.readTree(new URL(builder.buildURL()));

        System.out.println("Страница 1 скачена.");


        int totalRecords = node.path("recordCount").asInt(-1); //todo hardcode
        int pageSize = node.path("pageSize").asInt(-1); //todo hardcode
        if (pageSize < 0) {
            System.out.println("Ошибка предоставления данных(Кол-во страниц). Возможно API изменилось");
            throw new WrongPageSizeParam();
        }
        if (totalRecords < 0) {
            System.out.println("Не найдено записей по текущему запросу"); //nullable due API
            System.out.println("Возможно у интересующих Вас данных не указана дата.");
            throw new NoRecordsFoundException();
        }
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (!askForContinue(link, totalPages, totalRecords)) {
            throw new UserDeclinedException();
        }

        pagesAsJson.add(node);
        System.out.println("Начинаю скачивать остальные страницы..");
        System.out.println("Количество страниц: " + totalPages);
        System.out.println("Количество записей: " + totalRecords);

        for (int i = 2; i <= totalPages; i++) {
            try {
                System.out.printf("Скачивание страницы (%s/%s)%n", i, totalPages);
                System.out.println("Задержка между скачиваниями: " + ((double)delay / 1000) + " сек.");
                Thread.sleep(delay); //ensure we do not hack the gov
                builder.setPage(i);
                String pageUrl = builder.buildURL();
                JsonNode pageData = mapper.readTree(new URL(pageUrl));
                pagesAsJson.add(pageData);
            } catch (IOException e) {
                System.out.printf("Ошибка загрузки страницы (%s/%s)%n", i, totalPages);
                System.out.println("Данные не были сохранены.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Завершение процесса. Попробуйте вновь", e);
            }
        }
        System.out.println("Скачивание завершено");
        return new DataPacket(link, pagesAsJson);
    }

    private boolean askForContinue(Link link, int pages, int records) {
        if (System.getProperty("DontAsk") != null) return true;
        StringBuilder builder = new StringBuilder();
        Attribute dateFrom = link.getDateFrom();
        Attribute dateUpto = link.getDateUpto();
        builder.append(dateFrom != null ? "От: " + dateFrom.getValue() + "\n" : "");
        builder.append(dateUpto != null ? "До: " + dateUpto.getValue() + "\n" : "");
        builder.append("Всего страниц: ").append(pages);
        builder.append("\n");
        builder.append("Всего записей: ").append(records);
        Boolean b = Main.listener.askYesOrNo("Вы собираетесь загрузить данные: \n" + builder.toString());
        return b;
    }
}
