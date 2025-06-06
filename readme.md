## Описание приложения

### Задания №1 и №3

- Загрузка и сохранение файлов реализована в пакетах: `data` и `filemanager`.
- Данные загружаются постранично (по 100 объектов на страницу, `pageSize=100`, вместо значения по умолчанию `10`).
- На основе JSON-файла создаётся его XML-копия.  
  _(Создание XML можно отключить в классе DataSaver)_
- Все страницы архивируются и сохраняются в папке `data` в формате `.zip`.
- Данные сохраняются в raw-формате с заголовками, такими как `pageNum`.

### Задание №2

- Денормализация JSON реализована в классе `data/Denormalizer` с автоматической генерацией структуры.
- Массивы сохраняются в БД в виде JSON-строк.
- Кэш не используется: при каждой команде происходит повторная загрузка, перезапись `.zip` и данных в БД.

##  Используемый стек

- Java 8
- Maven
- PostgreSQL (через Docker)
- Jackson (JSON + XML)
- JUnit
- Docker + Docker Compose

##  Автоматизация

- Автоматическое развертывание PostgreSQL через `docker-compose`.
- По умолчанию используется стандартное подключение к PostgreSQL(Имя, пароль, хост)
>Название схемы: `data_fetcher`
><br>Название таблицы: `denormalized_data`  
>_(Переопределяется в `Repository.java`)_

##  JVM-флаги подключения к базе данных

| Флаг               | Описание                                                                        |
|--------------------|---------------------------------------------------------------------------------|
| `-DDB_HOST=...`     | Адрес хоста PostgreSQL-сервера (по умолчанию `localhost`)                       |
| `-DDB_PORT=...`     | Порт подключения к БД (по умолчанию `5432`)                                     |
| `-DDB_NAME=...`     | Название базы данных для подключения  (по умолчанию `postgres`)                 |
| `-DDB_USER=...`     | Имя пользователя БД       (по умолчанию `postgres`)                             |
| `-DDB_PASSWORD=...` | Пароль пользователя БД     (по умолчанию 'root')                                |

> Пример использования флагов запуска:  
> `java -DDB_HOST=localhost -DDB_PORT=5432 -DDB_NAME=mydb -DDB_USER=postgres -DDB_PASSWORD=pass -jar DataFetcher.jar`


## Доп сведения

##
| Флаг                   | Описание                                                                 |
|------------------------|--------------------------------------------------------------------------|
| `-DNoValidation=true`   | Отключает валидацию данных (например, формата даты)                     |
| `-DDontAsk=true`        | Отключает подтверждения пользователя (yes/no)                           |
| `-DColored=true`        | Включает цветную подсветку сообщений в консоли                          |

## Дополнительные фичи

- ✅ **№1**: В `.zip` включаются также `.xml`-файлы помимо основных `.json` файлов.
- ⛔ **№2**: Не выполнялось.
- ✅ **№3**: PostgreSQL разворачивается автоматически через `docker-compose`.

## Запуск
Требуется `Docker`:
- запусить run-Docker.bat дождаться развертывания докер-образа. 
 >Скрипт аналогичен команде `docker-compose up --build`


- Вставить нужные флаги в run-App.bat (Если требуется.)
> Если значения для подключения к БД стандартные, то можно их не указывать
```java
public DataBaseConnection(String host, String port, String db, String user, String pass) {
  this.host = host == null ? "localhost" : host;
  this.port = port == null ? "5432" : port;
  this.db = db == null ? "postgres" : db;
  this.user = user == null ? "postgres" : user;
  this.pass = pass == null ? "root" : pass;
  this.url = "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.db;
}
```
- Запускаем run-App.bat (Или в ручную) и приложение готово к использованию
## Использование
Принимает на вход аргументы в виде пары ключ-значение:

filterminloaddate `дата в формате дд.мм.гггг`<br>
filtermaxloaddate `дата в формате дд.мм.гггг`

У каждого аргумента должно быть свое значение.