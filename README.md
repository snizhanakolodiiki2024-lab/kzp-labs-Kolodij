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
| Кількість тестів | 7 | 35 |
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
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
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
- коди завершення програми.

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
- виконуються всі 35 тестів;
- запускається SpotBugs;
- перевіряється версія;
- JAR публікується як artifact.

Успішний workflow:

```text
https://github.com/snizhanakolodiiki2024-lab/kzp-labs-Kolodij/actions/runs/38091105340
```

Опубліковано 3 artifacts.

## GitHub Issues

| Issue | Зміст |
| --- | --- |
| `#6` | `UtilityReading` та рефакторинг |
| `#7` | `ConsumptionCost` та тести |
| `#8` | Maven, SpotBugs, JAR |
| `#9` | CI, artifacts, версія |

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
- кількість тестів збільшено до 35;
- SpotBugs не знаходить дефектів;
- CI проходить на Ubuntu, Windows і macOS;
- версію оновлено до `2.0.0`;
- JAR публікується як artifact.

Після завершення Pull Request буде створено Git-тег:

```text
v2.0.0
```