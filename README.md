# Лабораторна робота №1 — Комунальні показники

## Варіант

Варіант №9 — **«Комунальні показники»**.

Формат одного запису:

```text
meter;date;value;tariff
```

Приклад коректного запису:

```text
Електролічильник-1;2026-09-01;125.50;4.32
```

## Призначення програми

Консольна Java-програма читає дані з CSV-файла, перевіряє коректність записів, пропускає помилкові рядки з поясненням причини та формує підсумковий звіт.

Для коректних записів програма обчислює:

- кількість коректних записів;
- сумарне споживання;
- загальну вартість;
- найбільше споживання.

Вхідний файл за замовчуванням:

```text
data/input.csv
```

Вихідний файл за замовчуванням:

```text
out/report.txt
```

## Використані технології

У проєкті використовуються:

- Java 21;
- Maven;
- Maven Wrapper;
- JUnit 5;
- SpotBugs;
- Maven Shade Plugin;
- GitHub Actions.

## Запуск і перевірка

Запуск тестів:

```powershell
.\mvnw.cmd test
```

Повна перевірка з тестами та SpotBugs:

```powershell
.\mvnw.cmd verify
```

Створення виконуваного JAR:

```powershell
.\mvnw.cmd package
```

Після успішної збірки створюється:

```text
target/lab01-1.0.0.jar
```

Запуск програми:

```powershell
java -jar target\lab01-1.0.0.jar
```

Довідка:

```powershell
java -jar target\lab01-1.0.0.jar --help
```

Перевірка версії:

```powershell
java -jar target\lab01-1.0.0.jar --version
```

Локально:

```text
Version 1.0.0
```

У GitHub Actions додатково виводиться номер CI-збірки, наприклад:

```text
Version 1.0.0 (build 4)
```

Версія `1.0.0` визначає версію продукту, а `build 4` — номер конкретного запуску CI.

Використання власного вхідного та вихідного файла:

```powershell
java -jar target\lab01-1.0.0.jar --input data\input.csv --output reports\result.txt
```

На macOS та Ubuntu замість:

```powershell
.\mvnw.cmd
```

використовується:

```bash
./mvnw
```

## Тестування

Для автоматизованого тестування використовується JUnit 5.

Реалізовано 7 тестів, які перевіряють:

- коректні та некоректні записи;
- порожні поля;
- неправильну кількість полів;
- нечислові значення;
- від’ємні `value` і `tariff`;
- відсутність коректних записів;
- параметр `--version`;
- невідомий аргумент командного рядка.

Поточний результат:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Статичний аналіз

Для статичного аналізу використовується SpotBugs.

SpotBugs запускається під час:

```powershell
.\mvnw.cmd verify
```

Результат:

```text
BugInstance size is 0
Error size is 0
No errors/warnings found
BUILD SUCCESS
```

## GitHub Actions

Workflow знаходиться у файлі:

```text
.github/workflows/ci.yml
```

GitHub Actions запускається під час:

- `push`;
- `pull_request`.

Перевірка виконується на трьох операційних системах:

- Ubuntu;
- Windows;
- macOS.

На всіх трьох ОС використовується Java 21 та Maven Wrapper.

CI запускає Maven-фазу:

```text
verify
```

Таким чином автоматично перевіряються:

- компіляція;
- JUnit 5 тести;
- SpotBugs;
- пакування JAR.

Усі три CI jobs проходять успішно.

## CI build number і artifacts

У GitHub Actions використовується автоматичний номер запуску:

```text
github.run_number
```

Він передається програмі через змінну середовища `CI_BUILD_NUMBER`.

Тому в CI параметр:

```text
--version
```

виводить, наприклад:

```text
Version 1.0.0 (build 4)
```

Maven-залежності кешуються у GitHub Actions.

Після успішної перевірки виконуваний JAR публікується як workflow artifact.

Назва artifact містить операційну систему та номер CI-збірки, наприклад:

```text
jar-windows-latest-4
```

В artifact зберігається:

```text
lab01-1.0.0.jar
```

## Структура проєкту

```text
kzp-labs-Kolodij/
├── .github/
│   └── workflows/
│       └── ci.yml
├── .mvn/
│   └── wrapper/
├── ai/
│   ├── manager.md
│   ├── devops.md
│   ├── developer.md
│   ├── validator.md
│   ├── documenter.md
│   └── reviewer.md
├── data/
│   └── input.csv
├── src/
│   ├── main/
│   │   └── java/
│   │       └── ua/
│   │           └── lpnu/
│   │               └── kzp/
│   │                   └── Main.java
│   └── test/
│       └── java/
│           └── ua/
│               └── lpnu/
│                   └── kzp/
│                       └── MainTest.java
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
└── REPORT.md
```

## Ролі ШІ

У каталозі `ai/` створено ролі:

- `Manager` — планування та контроль вимог;
- `DevOps` — Maven, Git, SpotBugs, JAR і GitHub Actions;
- `Developer` — аналіз реалізації;
- `Validator` — перевірка крайових випадків;
- `Documenter` — перевірка README, REPORT і Javadoc;
- `Reviewer` — фінальна перевірка коду, тестів, CI та документації.

## Версія

Поточна версія програми:

```text
1.0.0
```

Фінальний Git-тег:

```text
v1.0.0
```

# Лабораторна робота №2 — Класи, record і інкапсуляція

## Варіант

Варіант №9 — **«Комунальні показники»**.

Формат вхідного запису залишається без змін:

```text
meter;date;value;tariff
```

Основна сутність предметної області — клас `UtilityReading`.

## Level 1 — базовий

У лабораторній роботі №2 виконано перехід від прямого опрацювання CSV-полів у `Main` до об'єктної моделі.

Створено клас `UtilityReading` з полями:

- `meter`;
- `date`;
- `value`;
- `tariff`.

Усі поля оголошені як `private final`.
Сеттери не використовуються.

## Валідація UtilityReading

Конструктор перевіряє:

- `meter` не є `null` або порожнім;
- `date` не є `null` або порожньою;
- `value` не є від'ємним;
- `tariff` не є від'ємним;
- `value` є скінченним числом;
- `tariff` є скінченним числом.

Для перевірки числових значень використовується `Double.isFinite()`, тому `NaN`, `Infinity` і `-Infinity` не допускаються.

## Фабричний метод fromCsv

У класі `UtilityReading` реалізовано статичний метод:

```java
UtilityReading.fromCsv(String line)
```

Метод:

- перевіряє структуру CSV-рядка;
- очікує 4 поля;
- перетворює `value` і `tariff` у числові значення;
- створює об'єкт `UtilityReading`.

Перевірка інваріантів виконується конструктором і не дублюється в `Main`.

## Рефакторинг Main

`Main` тепер працює зі списком об'єктів `UtilityReading`.

Некоректний запис не додається до списку і не впливає на статистику.
Повідомлення про помилку зберігає номер відповідного рядка.

Також виправлено зауваження після ЛР1:

- додано перевірку скінченності `value` і `tariff`;
- усунуто зайву конкатенацію рядків;
- для помилкового завершення передбачено ненульовий код завершення;
- успішне завершення використовує код `0`.

## Сумісність з лабораторною роботою №1

Формат вхідних даних і підсумкового звіту не змінено.

Для контрольного файла результат після рефакторингу:

```text
Результати:
Кількість коректних записів: 3
Сумарне споживання: 186.25
Загальна вартість: 1198.32
Найбільше споживання: 125.50
```

Старі 7 тестів лабораторної роботи №1 продовжують проходити:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Структура після Level 1

```text
src/main/java/ua/lpnu/kzp/
├── Main.java
└── UtilityReading.java

src/test/java/ua/lpnu/kzp/
└── MainTest.java
```