package org.mrdarkimc.DataFetcher.Logger;

public class Logger {
    public static void showHint(){
        System.out.println(Color.success + "Примеры использования: ");
        System.out.println(Color.reset + "Отобразить данные загруженые с 10.03.22 по 10.04.22 " + Color.success + "filtermaxloaddate 10.04.2022 filterminloaddate 10.03.2022");
        System.out.println(Color.reset + "Отобразить данные загруженые с 10.03.22 " + Color.success + "filterminloaddate 10.03.2022");
        System.out.println(Color.reset + "Отобразить данные загруженые до 10.03.22 " + Color.success + "filtermaxloaddate 10.04.2022" + Color.reset);
    }
}
