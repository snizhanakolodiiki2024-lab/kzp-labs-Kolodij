Лабораторна робота №1 — Комунальні показники

Варіант

Варіант №9 — «Комунальні показники».

Формат одного запису:

meter;date;value;tariff

Приклад:

Електролічильник-1;2026-09-01;125.50;4.32

Призначення програми

Консольна Java-програма читає дані з CSV-файла, перевіряє записи, пропускає некоректні рядки з поясненням причини та формує підсумковий звіт.

Для коректних записів обчислюються:

кількість коректних записів;

сумарне споживання;

загальна вартість;

найбільше споживання.

Вхідний файл за замовчуванням:

data/input.csv

Вихідний файл за замовчуванням:

out/report.txt

Запуск

Проєкт використовує Java 21 і Maven Wrapper.

Тести:

.\mvnw.cmd test

Повна перевірка з SpotBugs:

.\mvnw.cmd verify

Створення виконуваного JAR:

.\mvnw.cmd package

Запуск:

java -jar target\lab01-1.0.0.jar

Довідка:

java -jar target\lab01-1.0.0.jar --help

Версія:

java -jar target\lab01-1.0.0.jar --version

Результат:

Комунальні показники 1.0.0

Власний вхідний і вихідний файл:

java -jar target\lab01-1.0.0.jar --input data\input.csv --output reports\result.txt

На macOS та Ubuntu замість .\mvnw.cmd використовується ./mvnw.

Тестування

Використовується JUnit 5.

Реалізовано 7 тестів, які перевіряють:

коректні та некоректні записи;

порожні поля та неправильну кількість полів;

нечислові значення;

від’ємні value і tariff;

відсутність коректних записів;

--version;

невідомий аргумент.

Поточний результат:

Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

CI

Налаштовано GitHub Actions у:

.github/workflows/ci.yml

Workflow запускає verify на:

Ubuntu;

Windows;

macOS.

На всіх трьох ОС використовується Java 21 і Maven Wrapper. Після успішної перевірки JAR публікується як workflow artifact.

Структура

Основні файли:

.github/workflows/ci.yml
ai/
data/input.csv
src/main/java/ua/lpnu/kzp/Main.java
src/test/java/ua/lpnu/kzp/MainTest.java
pom.xml
mvnw
mvnw.cmd
README.md
REPORT.md

У ai/ створено ролі:

Manager;

DevOps;

Developer;

Validator;

Documenter;

Reviewer.