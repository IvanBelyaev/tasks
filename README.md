# Система управления задачами

REST-сервис для управления задачами, позволяет выполнять CRUD операции
Слой сервиса покрыт unit тестами, для контроллера написаны интеграционные тесты

Для запуска проекта нужно настроить подключение к БД в файле application.properties
При первом запуске приложения нужно установить в настройку spring.jpa.properties.hibernate.hbm2ddl.auto значение create
для того чтобы автоматически создались нужные таблицы, потом можно заменить на validate

Тестировать можно через OpenApi http://localhost:8080/swagger-ui/index.html