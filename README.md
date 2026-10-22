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

# Лабораторна робота №2

Поточна версія: `2.0.0`.

**Варіант:** №9
**Предметна область:** комунальні показники

У лабораторній роботі №2 виконано перехід від рядкового представлення даних до об'єктної моделі.

## Основні класи

### `UtilityReading`

Основна сутність:

```java
UtilityReading
```

Поля:

```java
private final String meter;
private final String date;
private final double value;
private final double tariff;
```

Особливості:

- клас `final`;
- поля `private final`;
- setter-и відсутні;
- валідація виконується в конструкторі;
- `NaN`, `Infinity` і `-Infinity` не допускаються;
- для перевірки чисел використовується `Double.isFinite()`.

Створення з CSV:

```java
UtilityReading.fromCsv(String line)
```

Формат CSV:

```text
meter;date;value;tariff
```

### `ConsumptionCost`

Допоміжне незмінне значення:

```java
record ConsumptionCost(double consumption, double cost)
```

Record використовується під час обчислення споживання та вартості.

## Порівняння Lab 1 і Lab 2

| Характеристика | Lab 1 | Lab 2 |
| --- | --- | --- |
| Представлення запису | рядок та окремі змінні | `UtilityReading` |
| Розбір CSV | у `Main` | `UtilityReading.fromCsv()` |
| Валідація | у циклі обробки | у конструкторі |
| Дані | окремі значення | `List<UtilityReading>` |
| Допоміжні значення | окремі змінні | `ConsumptionCost` |
| Перевірка `NaN` / Infinity | неповна | `Double.isFinite()` |
| Створення об'єкта | конструктор | конструктор + `Builder` |
| Кількість тестів | 7 | 38 |
| Версія | `1.0.0` | `2.0.0` |
| JAR | `lab01-1.0.0.jar` | `lab01-2.0.0.jar` |

## Результати

Для контрольного набору даних:

```text
Кількість коректних записів: 3
Сумарне споживання: 186.25
Загальна вартість: 1198.32
Найбільше споживання: 125.50
```

Результати Lab 1 і Lab 2 однакові, тому зовнішня поведінка програми після рефакторингу збережена.

## Тестування

Запуск:

```powershell
.\mvnw.cmd test
```

Результат:

```text
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Перевіряються:

- коректні та граничні значення;
- `null` і blank;
- від'ємні значення;
- `NaN`;
- `Infinity`;
- `-Infinity`;
- `fromCsv()`;
- `ConsumptionCost`;
- коди завершення програми;
- створення об'єкта через `Builder`;
- валідація під час використання `Builder`.

## Maven і SpotBugs

Повна перевірка:

```powershell
.\mvnw.cmd clean verify
```

Результат:

```text
BugInstance size is 0
Error size is 0
No errors/warnings found
BUILD SUCCESS
```

## Незмінність і Builder ⭐

`UtilityReading` є повністю незмінним:

- клас `final`;
- поля `private final`;
- setter-и відсутні.

Для зручного створення об'єктів додано:

```java
UtilityReading.Builder
```

Приклад:

```java
UtilityReading reading = UtilityReading.builder()
        .meter("Водомір-1")
        .date("2026-09-01")
        .value(10.0)
        .tariff(2.5)
        .build();
```

Метод `build()` викликає основний конструктор `UtilityReading`, тому Builder не обходить валідацію.

Для Builder додано 3 тести:

- коректне створення;
- відхилення некоректного значення;
- відхилення відсутніх обов'язкових полів.

## JAR

Поточний JAR:

```text
target/lab01-2.0.0.jar
```

Перевірка версії:

```powershell
java -jar target\lab01-2.0.0.jar --version
```

Результат:

```text
Version 2.0.0
```

Запуск програми:

```powershell
java -jar target\lab01-2.0.0.jar --input data\input.csv --output out\report-lab2.txt
```

## GitHub Actions

CI запускається на трьох ОС:

| ОС | Результат |
| --- | --- |
| Ubuntu | Success |
| Windows | Success |
| macOS | Success |

У workflow:

- запускається Maven `verify`;
- виконуються автоматизовані тести;
- запускається SpotBugs;
- перевіряється версія;
- JAR публікується як artifact.

Успішний workflow Level 3:

```text
https://github.com/snizhanakolodiiki2024-lab/kzp-labs-Kolodij/actions/runs/38093321839
```

Опубліковано 3 artifacts.

## GitHub Issues

| Issue | Зміст |
| --- | --- |
| `#6` | `UtilityReading` та рефакторинг |
| `#7` | `ConsumptionCost` та тести |
| `#8` | Maven, SpotBugs, JAR |
| `#9` | CI, artifacts, версія |
| `#10` | Immutable `UtilityReading` та Builder ⭐ |

## Pull Request

Фінальний Pull Request:

```text
https://github.com/snizhanakolodiiki2024-lab/kzp-labs-Kolodij/pull/11
```

CI на Pull Request успішно пройшов на Ubuntu, Windows і macOS.

## Структура Lab 2

```text
src/
├── main/java/ua/lpnu/kzp/
│   ├── Main.java
│   ├── UtilityReading.java
│   └── ConsumptionCost.java
└── test/java/ua/lpnu/kzp/
    ├── MainTest.java
    ├── UtilityReadingTest.java
    └── ConsumptionCostTest.java
```

## Підсумок

У Lab 2:

- створено `UtilityReading`;
- додано `fromCsv()`;
- реалізовано `ConsumptionCost`;
- валідацію перенесено до предметної моделі;
- додано перевірку скінченних чисел;
- `UtilityReading` залишено повністю незмінним;
- реалізовано `UtilityReading.Builder`;
- Builder не обходить валідацію конструктора;
- кількість тестів збільшено до 38;
- SpotBugs не знаходить дефектів;
- CI проходить на Ubuntu, Windows і macOS;
- версію оновлено до `2.0.0`;
- JAR публікується як artifact.

Git-тег фінальної версії:

```text
v2.0.0
```

Посилання:

```text
https://github.com/snizhanakolodiiki2024-lab/kzp-labs-Kolodij/tree/v2.0.0
```

# Лабораторна робота №3

Поточна версія: `3.0.0`.

**Варіант:** №9  
**Предметна область:** комунальні показники

У Lab 3 модель розширено за допомогою успадкування, enum і поліморфізму.

## Основні зміни

Створено ієрархію:

```text
UtilityReading
├── ElectricityReading
└── WaterReading
```

`UtilityReading` став абстрактним базовим класом і містить спільні поля та валідацію.

`ElectricityReading` і `WaterReading` є різновидами `UtilityReading` (`is-a`), тому для них використано спадкування.

Підтипи мають власні правила:

- `ElectricityReading` — використовує `tariffCoefficient`;
- `WaterReading` — використовує `serviceFee`.

Додано enum:

```java
UtilityKind
```

зі значеннями:

```text
ELECTRICITY
WATER
```

## Поліморфізм

У базовому класі оголошено:

```java
public abstract double calculateCost();
```

Підтипи мають різні реалізації цього методу.

Об'єкти зберігаються у:

```java
List<UtilityReading>
```

і обробляються через:

```java
reading.calculateCost();
```

без `if` або `switch` за конкретним підтипом.

## equals() і hashCode()

Реалізовано логічну рівність об'єктів через:

```java
equals()
hashCode()
```

Рівність перевірено за допомогою:

```java
HashSet<UtilityReading>
```

Рівні об'єкти не створюють дублікати у `HashSet`.

## Порівняння Lab 2 і Lab 3

| Характеристика | Lab 2 | Lab 3 |
| --- | --- | --- |
| Базова модель | один `UtilityReading` | абстрактний `UtilityReading` |
| Типи показників | один клас | `ElectricityReading`, `WaterReading` |
| Категорія | визначалась із даних | `UtilityKind` |
| Розрахунок вартості | `value * tariff` у `Main` | поліморфний `calculateCost()` |
| Обробка | `List<UtilityReading>` | `List<UtilityReading>` з різними підтипами |
| Рівність | стандартна `Object` | `equals()` + `hashCode()` |
| Перевірка колекції | не використовувалась | `HashSet` |
| Кількість тестів | 38 | 58 |
| Версія | `2.0.0` | `3.0.0` |
| JAR | `lab01-2.0.0.jar` | `lab01-3.0.0.jar` |

## Сумісність із Lab 2

Формат CSV не змінено:

```text
meter;date;value;tariff
```

Контрольний результат також залишився без змін:

```text
Кількість коректних записів: 3
Сумарне споживання: 186.25
Загальна вартість: 1198.32
Найбільше споживання: 125.50
```

## Тестування

Запуск:

```powershell
.\mvnw.cmd test
```

Результат:

```text
Tests run: 58, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Повна перевірка:

```powershell
.\mvnw.cmd verify
```

SpotBugs:

```text
BugInstance size is 0
Error size is 0
BUILD SUCCESS
```

## JAR

Створення:

```powershell
.\mvnw.cmd package
```

Поточний JAR:

```text
target/lab01-3.0.0.jar
```

Перевірка версії:

```powershell
java -jar target\lab01-3.0.0.jar --version
```

Результат:

```text
Version 3.0.0
```
## Додаткове завдання ⭐ — sealed-ієрархія

Базовий тип оголошено як `sealed`:

```java
public abstract sealed class UtilityReading
        permits ElectricityReading, WaterReading
```

Тому безпосередніми підтипами можуть бути лише:

```text
ElectricityReading
WaterReading
```

Для демонстрації вичерпної перевірки використано pattern matching у `switch`:

```java
return switch (reading) {
    case ElectricityReading electricity -> ...
    case WaterReading water -> ...
};
```

Гілка `default` не потрібна, оскільки компілятор знає повний перелік дозволених підтипів.

`switch` не дублює логіку `calculateCost()`: він використовується лише для отримання специфічного стану підтипу.

Для sealed-ієрархії додано окремі тести. Загальна кількість тестів — 58.

## Підсумок

У Lab 3:

- створено абстрактний `UtilityReading`;
- додано `ElectricityReading` і `WaterReading`;
- додано `UtilityKind`;
- реалізовано поліморфний `calculateCost()`;
- реалізовано `equals()` і `hashCode()`;
- рівність перевірено через `HashSet`;
- кількість тестів збільшено до 58;
- збережено результати попередньої роботи;
- версію оновлено до `3.0.0`.

## Лабораторна робота №4 — Stream API

У лабораторній роботі №4 розпочато перенесення обробки колекції `List<UtilityReading>` із циклового підходу до декларативних запитів Java Stream API.

Для потокових запитів створено окремий клас `UtilityReadingQueries`.

На базовому рівні реалізовано три запити:

- `filterAboveThreshold()` — відбір показників, у яких `value` перевищує заданий поріг, через `filter`;
- `consumptionValues()` — перетворення колекції показників у список значень споживання через `map`;
- `sumByMeter()` — групування показників за лічильником і обчислення сумарного споживання через `Collectors.groupingBy` та `Collectors.summingDouble`.

Запити працюють зі спільним типом `UtilityReading`, не використовують перевірок конкретних підтипів і не змінюють вхідну колекцію.

Після реалізації Level 1 усі попередні та нові тести проходять успішно:

`Tests run: 65, Failures: 0, Errors: 0, Skipped: 0`

Контрольний результат попередньої лабораторної роботи залишився незмінним:

- кількість коректних записів — `3`;
- сумарне споживання — `186.25`;
- загальна вартість — `1198.32`;
- найбільше споживання — `125.50`.