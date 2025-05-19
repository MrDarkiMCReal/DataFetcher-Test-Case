package org.mrdarkimc.DataFetcher;

import org.mrdarkimc.DataFetcher.Logger.Color;
import org.mrdarkimc.DataFetcher.Logger.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
public class Validator {
    public static void validateDateFormat(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        dateTimeFormatter.withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException ignore) {
            System.out.println(Color.error + "Неверный формат даты. Дата записывается в формате дд.мм.гггг" + Color.reset);
            Logger.showHint();
            throw new IllegalArgumentException();
        }
    }


    public static void isCorrectInput(String... input) throws IllegalArgumentException {
        if (input.length % 2 != 0) {
            System.out.println(Color.error + "Неверный ввод данных. Требуется вводить название фильтра и значенение фильтра через пробел" + Color.reset);
            throw new IllegalArgumentException();
        }
    }
}
