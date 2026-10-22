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

Лабораторна робота №4 — Stream API
Поточна версія: 4.0.0.

Варіант: №9
Предметна область: комунальні показники

У лабораторній роботі №4 основні операції обробки поліморфної колекції List<UtilityReading> перенесено до декларативних конвеєрів Java Stream API. Предметна модель лабораторної роботи №3, формат CSV та зовнішній контрольний звіт збережені.
Основні зміни
Для потокових запитів створено окремий клас:
UtilityReadingQueries
Реалізовано п'ять основних запитів і окремий пошук через Optional:
1. filterAboveThreshold() — відбір записів, у яких value > threshold;
2. consumptionValues() — перетворення UtilityReading у список значень споживання;
3. sumByMeter() — групування за лічильником і сумування споживання;
4. valueStatistics() — статистика значень через DoubleSummaryStatistics;
5. topByValue() — top-N за значенням спаданням із додатковим сортуванням за датою;
6. findByMeter() — пошук першого запису за назвою лічильника з результатом Optional<UtilityReading>.
Запити працюють зі спільним базовим типом UtilityReading, не виконують if або switch за конкретним підтипом і не змінюють вхідну колекцію.
1. Фільтрація за порогом
Метод:
UtilityReadingQueries.filterAboveThreshold(readings, threshold)
Конвеєр:
List<UtilityReading>
    -> stream()
    -> filter(value > threshold)
    -> toList()
Умова є строгою: значення, яке дорівнює порогу, не входить до результату.
2. Перетворення значень
Метод:
UtilityReadingQueries.consumptionValues(readings)
Конвеєр:
List<UtilityReading>
    -> stream()
    -> map(UtilityReading::getValue)
    -> toList()
Результат:
List<Double>
3. Групування за лічильником
Метод:
UtilityReadingQueries.sumByMeter(readings)
Використано:
Collectors.groupingBy(
        UtilityReading::getMeter,
        Collectors.summingDouble(UtilityReading::getValue)
)
Результат:
Map<String, Double>
Ключем є meter, значенням — сумарне споживання для цього лічильника.
4. Статистика значень
Метод:
UtilityReadingQueries.valueStatistics(readings)
Використано:
Collectors.summarizingDouble(UtilityReading::getValue)
Результат:
DoubleSummaryStatistics
Через один collector отримуються:
- кількість записів;
- сума;
- мінімум;
- максимум;
- середнє значення.
Для порожньої колекції:
count = 0
sum = 0.0
average = 0.0
min = Infinity
max = -Infinity
У Main статистика викликається лише після перевірки, що список коректних записів не порожній.
5. Top-N
Метод:
UtilityReadingQueries.topByValue(readings, n)
Компаратор:
Comparator.comparingDouble(UtilityReading::getValue)
        .reversed()
        .thenComparing(UtilityReading::getDate)
Порядок:
1. value — за спаданням;
2. якщо value однакове — date за зростанням;
3. після сортування застосовується limit(n).
При n = 0 повертається порожній список. Якщо n більший за кількість записів, повертаються всі записи в правильному порядку. Від'ємне n відхиляється через IllegalArgumentException.
6. Optional-пошук
Метод:
UtilityReadingQueries.findByMeter(readings, meter)
Конвеєр:
stream()
    -> filter(meter matches)
    -> findFirst()
Результат:
Optional<UtilityReading>
Якщо лічильник знайдено, Optional містить перший відповідний запис. Якщо відповідного лічильника немає, повертається Optional.empty().
Основні розрахунки Main через Stream API
У Lab 3 підсумкові значення обчислювалися циклом for.

У Lab 4 основні обчислення звіту перенесено на Stream API.

Сума і максимум споживання отримуються через:
DoubleSummaryStatistics statistics =
        UtilityReadingQueries.valueStatistics(readings);

double totalConsumption = statistics.getSum();
double maxConsumption = statistics.getMax();
Загальна вартість визначається поліморфно:
double totalCost = readings.stream()
        .mapToDouble(UtilityReading::calculateCost)
        .sum();
Виклик calculateCost() залишається поліморфним: Java вибирає реалізацію ElectricityReading або WaterReading відповідно до фактичного типу об'єкта.

Цикл читання CSV залишено, оскільки він виконує послідовну обробку рядків, нумерацію та окреме повідомлення про помилку для кожного некоректного запису.
Додаткове завдання ⭐ — дворівневе groupingBy
Реалізовано додатковий метод:
UtilityReadingQueries.sumByKindAndMeter(readings)
Результат:
Map<UtilityKind, Map<String, Double>>
Використано groupingBy усередині groupingBy:
Collectors.groupingBy(
        UtilityReading::getKind,
        Collectors.groupingBy(
                UtilityReading::getMeter,
                Collectors.summingDouble(UtilityReading::getValue)
        )
)
Перший рівень групування — UtilityKind, другий — meter, після чого для кожної групи обчислюється сумарне значення value.

Для додаткового запиту перевірено також порожній вхід.
Незмінність вхідної колекції ⭐
Додано окремий тест, який послідовно викликає всі основні запити для змінного ArrayList і перевіряє, що порядок та склад початкової колекції залишаються незмінними.

Stream-запити повертають нові результати та не виконують мутацію вхідного списку.
Порівняння циклу і Stream API
Обчислення	Lab 3	Lab 4
Сума споживання	накопичення в for	DoubleSummaryStatistics.getSum()
Максимум	Math.max() у циклі	DoubleSummaryStatistics.getMax()
Загальна вартість	накопичення в for	mapToDouble(...).sum()
Фільтрація	ручна умова в циклі	filter()
Перетворення	ручне додавання в список	map()
Групування	ручна робота з Map	groupingBy() + summingDouble()
Top-N	ручне сортування/обрізання	sorted() + limit()
Пошук	ручний цикл і null/ознака	findFirst() + Optional


Обидва підходи мають лінійну складність для простих проходів по колекції. Для top-N поточна реалізація спочатку сортує колекцію, тому має складність приблизно O(n log n). Перевага Stream API в цій роботі — декларативність, композиція операцій та відсутність зовнішнього змінного стану.
Контрольний результат
Формат CSV не змінено:
meter;date;value;tariff
Після рефакторингу контрольний результат Lab 3 повністю збережено:
Результати:
Кількість коректних записів: 3
Сумарне споживання: 186.25
Загальна вартість: 1198.32
Найбільше споживання: 125.50
Результати до та після переходу на Stream API порівнювалися через Compare-Object; відмінностей немає.
Тестування
Після Lab 3 було:
58 тестів
Після реалізації Lab 4:
Tests run: 76, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Перевіряються:
- filter для звичайного випадку;
- точне граничне значення порогу;
- порожня колекція;
- map;
- групування та дубльований ключ meter;
- DoubleSummaryStatistics;
- статистика порожньої колекції;
- top-N;
- однакове основне значення та tie-break за датою;
- n = 0;
- n, що перевищує розмір колекції;
- успішний Optional-пошук;
- відсутній результат Optional.empty();
- дубльований meter і повернення першого запису;
- відсутність мутації вхідної колекції;
- дворівневе групування ⭐;
- порожній результат дворівневого групування;
- усі попередні сценарії Lab 1–3.
Maven, SpotBugs і JAR
Запуск тестів:
.\mvnw.cmd test
Повна перевірка:
.\mvnw.cmd clean verify
Пакування:
.\mvnw.cmd package
SpotBugs:
BugInstance size is 0
Error size is 0
BUILD SUCCESS
Поточний JAR:
target/lab01-4.0.0.jar
Запуск:
java -jar target\lab01-4.0.0.jar --input data\input.csv --output out\report-lab4.txt
Перевірка версії:
java -jar target\lab01-4.0.0.jar --version
Локальний результат:
Version 4.0.0
Єдине джерело версії
Після зауваження викладача в Lab 4 усунено дублювання номера версії.

Версія продукту задається лише в:
pom.xml
Maven фільтрує ресурс:
src/main/resources/version.properties
де використовується:
app.version=${project.version}
Main читає значення app.version із ресурсу під час виконання. Тести не містять конкретного номера версії.

GitHub Actions також не містить жорстко заданого імені JAR. Воно визначається автоматично через:
project.build.finalName
Тому для наступної версії достатньо змінити номер версії продукту в одному місці — pom.xml.
GitHub Actions
CI запускається на:
ОС	Результат
Ubuntu	Success
Windows	Success
macOS	Success


На кожній ОС:
- використовується Java 21;
- запускається Maven Wrapper;
- виконується verify;
- запускаються всі 76 тестів;
- виконується SpotBugs;
- автоматично визначається ім'я JAR;
- виконується --version;
- JAR завантажується як workflow artifact.
Для повного запуску matrix отримано 3 artifacts — по одному для Ubuntu, Windows і macOS.

У CI версія може виглядати так:
Version 4.0.0 (build N)
де N — github.run_number.
GitHub Issues
Для Lab 4 створено:
Issue	Зміст
#17	filter, map, groupingBy
#18	статистика, top-N, Optional, тести
#19	Maven, JAR та автоматичне версіонування
#20	CI, документація та реліз


Issues залишаються відкритими до фінального merge та будуть закриті через Closes у Pull Request.
Pull Request і реліз
Робоча гілка:
lab4
Фінальний Pull Request створюється з:
lab4 -> main
У його описі потрібно вказати:
Closes #17
Closes #18
Closes #19
Closes #20
Після зеленого CI на Pull Request зміни зливаються у main.

Фінальний релізний тег після merge:
v4.0.0
Структура Lab 4
src/
├── main/
│   ├── java/ua/lpnu/kzp/
│   │   ├── Main.java
│   │   ├── UtilityReading.java
│   │   ├── ElectricityReading.java
│   │   ├── WaterReading.java
│   │   ├── UtilityKind.java
│   │   ├── ConsumptionCost.java
│   │   └── UtilityReadingQueries.java
│   └── resources/
│       └── version.properties
└── test/java/ua/lpnu/kzp/
    ├── MainTest.java
    ├── UtilityReadingTest.java
    ├── ElectricityReadingTest.java
    ├── WaterReadingTest.java
    ├── UtilityKindTest.java
    ├── PolymorphismTest.java
    ├── UtilityReadingEqualityTest.java
    ├── SealedHierarchyTest.java
    └── UtilityReadingQueriesTest.java
Підсумок Lab 4
У Lab 4:
- збережено поліморфну модель Lab 3;
- створено UtilityReadingQueries;
- реалізовано всі 5 обов'язкових Stream API запитів;
- реалізовано Optional-пошук;
- основні обчислення звіту в Main переведено на Stream API;
- збережено поліморфний calculateCost();
- перевірено межу фільтра, дублікати, tie-break, n = 0, завелике n та порожні набори;
- додано окремий тест відсутності мутації;
- реалізовано дворівневий groupingBy ⭐;
- кількість тестів збільшено з 58 до 76;
- SpotBugs не виявляє дефектів;
- контрольний звіт Lab 3 не змінився;
- версію оновлено до 4.0.0;
- pom.xml став єдиним джерелом номера версії;
- CI успішно працює на Ubuntu, Windows і macOS;
- для повного matrix-run опубліковано 3 JAR artifacts.