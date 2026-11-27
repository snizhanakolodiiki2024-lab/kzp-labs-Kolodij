# Лабораторна робота №6

**Дисципліна:** Кросплатформні засоби програмування  
**Варіант:** №9  
**Предметна область:** комунальні показники  
**Python:** 3.12.10  
**Версія:** `6.0.0`  
**Гілка:** `lab6`

## Основні зміни

У Lab 6 реалізовано Python-застосунок для читання, валідації та аналізу комунальних показників із CSV, формування текстового звіту та запуску через CLI.

Предметну область Lab 1–5 збережено. Python-проєкт розміщено в окремому каталозі `python/`.

Створено:

```text
loader.py
analysis.py
errors.py
report.py
cli.py
streaming.py
benchmark.py
pyproject.toml
```

Java-застосунок та попередню інфраструктуру збережено.

## Python та віртуальне середовище

Конфігурація проєкту:

```text
pyproject.toml
```

Налаштовано:

- пакет `kzp-utility-report`;
- версію `6.0.0`;
- Python 3.11+;
- dev-залежності pytest, Ruff та build;
- CLI entry point `kzp-report`.

Створення середовища:

```powershell
cd python
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -e ".[dev]"
```

## CSV та UTF-8

Для Lab 6 використано CSV-дані попередньої лабораторної роботи.

Вхідний файл:

```text
python/data/input.csv
```

Заголовок:

```csv
meter,date,value,tariff
```

Поля після перетворення:

```text
meter  -> str
date   -> datetime.date
value  -> float
tariff -> float
```

CSV читається через:

```python
csv.DictReader
Path.open
```

З параметрами:

```python
encoding = "utf-8"
newline = ""
```

Контрольний набір містить 6 записів та 5 унікальних лічильників.

Підтримуються:

- коми;
- подвійні лапки;
- `LF`;
- `CRLF`;
- multiline fields.

## Loader та DataError

Основні функції:

```python
parse_text()
parse_number()
parse_date()
load_records()
iter_records()
```

Перевіряються:

- некоректний заголовок CSV;
- відсутні та зайві поля;
- порожній `meter`;
- неправильні числа;
- неправильні дати;
- від'ємні значення;
- `NaN`;
- `Infinity`;
- `-Infinity`;
- пошкоджений CSV.

Створено власний виняток:

```python
DataError
```

Першопричина зберігається через:

```python
raise DataError(message) from error
```

Приклад повідомлення:

```text
Помилка даних: Рядок 2: поле value має бути числом
```

## Колекції та аналіз даних

Використано:

```python
list
dict
set
```

Основні функції:

```python
filter_above_threshold()
sum_by_meter()
unique_meters()
total_cost()
max_reading()
summarize()
```

Типовий поріг:

```python
DEFAULT_THRESHOLD = 75.0
```

List comprehension:

```python
[record for record in records if float(record["value"]) > threshold]
```

Set comprehension:

```python
{str(record["meter"]) for record in records}
```

Вартість споживання:

```text
cost = value * tariff
```

Функція `summarize()` підтримує:

- типовий параметр `threshold`;
- `*args` через `*metrics`;
- `**kwargs` через `**labels`.

Приклад:

```python
summarize(
    records,
    "value",
    "cost",
    threshold=75.0,
    value="Consumption",
    cost="Cost",
)
```

Результат:

```text
count: 2
Consumption: 206.25
Cost: 891.00
```

## Текстовий звіт та CLI

Основні функції:

```python
display_meter()
format_report()
write_report()
```

Запис звіту виконується через:

```python
with path.open("w", encoding="utf-8", newline="\n") as target:
    target.write(text)
```

Результат має детермінований порядок та форматування чисел до двох знаків після крапки.

CLI підтримує:

```text
input_csv
output_report
--threshold
```

Коди завершення:

| Код | Значення |
|---|---|
| `0` | Успішне виконання |
| `2` | Очікувана помилка |

Вхідний CSV не змінюється.

## Додаткове завдання ⭐

Реалізовано потокову обробку великого CSV без завантаження всіх записів у пам'ять.

Створено:

```text
streaming.py
benchmark.py
```

Основні функції:

```python
iter_records()
analyze_in_memory()
analyze_streaming()
generate_large_csv()
measure()
results_match()
```

Використано:

- `yield`;
- `itertools.islice()`;
- `itertools.cycle()`;
- `contextlib.closing()`;
- `tracemalloc`;
- `perf_counter()`;
- `math.isclose()`.

Запуск експерименту:

```powershell
python -m kzp_lab06.benchmark
```

Контрольний набір:

```text
100000 записів
1024 записи в порції
5 унікальних лічильників
5.58 MiB
```

Фактичне вимірювання:

```text
IN-MEMORY ANALYSIS
Peak memory: 35.268 MiB
Execution time: 4.118 s

STREAMING ANALYSIS
Peak memory: 0.792 MiB
Execution time: 3.153 s
```

Результат:

```text
Results match: True
Memory reduction: 97.75%
Memory ratio: 44.52x
```

Обидва алгоритми отримали однакові результати в межах заданої точності.

Для контрольного набору пікове використання пам'яті Python зменшено на 97.75%.

## Запуск

Усі команди виконуються з каталогу `python`.

Запуск Python-модуля:

```powershell
python -m kzp_lab06.cli data/input.csv reports/summary.txt
```

Запуск через CLI:

```powershell
kzp-report data/input.csv reports/summary.txt
```

Запуск із власним порогом:

```powershell
kzp-report data/input.csv reports/summary.txt --threshold 100
```

Довідка:

```powershell
kzp-report --help
```

Створюється:

```text
python/reports/summary.txt
```

Контрольний результат:

```text
Кількість записів: 6
Кількість унікальних лічильників: 5
Сумарне споживання: 384.75
Загальна вартість: 2876.81 грн
Найбільший показник: 125.50

ВІДБІР ЗАПИСІВ: value > 75.00
Кількість відібраних записів: 2
Сумарне споживання: 206.25
Вартість споживання: 891.00 грн
```

## Тестування

Після основної частини Lab 6:

```text
56 passed
```

Після додаткового завдання:

```text
74 passed in 2.93s
```

| Модуль | Кількість тестів |
|---|---:|
| Loader | 22 |
| Analysis | 19 |
| Report | 4 |
| CLI | 11 |
| Streaming | 10 |
| Benchmark | 8 |
| Усього | 74 |

Перевірено:

- читання CSV та UTF-8;
- CSV escaping та multiline fields;
- перетворення типів;
- порожній CSV;
- неправильні числа та дати;
- `NaN` та Infinity;
- відбір за порогом;
- агрегування;
- унікальні значення;
- `*args` та `**kwargs`;
- `DataError` та `__cause__`;
- формування текстового звіту;
- CLI та коди завершення;
- генератори;
- потокову обробку;
- різні розміри порцій;
- генерацію великого CSV;
- еквівалентність алгоритмів;
- вимірювання пам'яті та часу.

## Pytest, Ruff та wheel

Тести:

```powershell
python -m pytest
```

Перевірка якості коду:

```powershell
ruff check .
ruff format --check .
```

Пакування:

```powershell
python -m build
```

Фінальний локальний результат:

```text
74 passed
All checks passed!
```

Wheel:

```text
dist/kzp_utility_report-6.0.0-py3-none-any.whl
```

Source distribution:

```text
dist/kzp_utility_report-6.0.0.tar.gz
```

На інженерному етапі перевірено встановлення wheel в окремому середовищі `.venv-wheel` без dev-залежностей.

Порівняння хешів звітів:

```text
True
```

Після додавання зірочки оновлений wheel успішно зібрано в CI.

## GitHub Actions

Створено окремий workflow:

```text
.github/workflows/python-ci.yml
```

CI налаштовано на:

| ОС | Перевірка |
|---|---|
| Ubuntu | Python checks |
| Windows | Python checks |
| macOS | Python checks |

На кожній ОС виконуються:

- Python 3.12;
- встановлення залежностей;
- 74 тести pytest;
- Ruff check;
- Ruff format check;
- успішний та помилковий сценарії CLI;
- збірка wheel;
- публікація artifacts.

Кожний artifact містить:

```text
kzp_utility_report-6.0.0-py3-none-any.whl
```

Фінальні результати:

| ОС | Java CI #76 | Python CI #3 |
|---|---|---|
| Ubuntu | Success | Success |
| Windows | Success | Success |
| macOS | Success | Success |

Java CI попередніх лабораторних робіт збережено.

У Pull Request:

```text
12 successful checks
No conflicts with base branch
```

## GitHub Issues

Для Lab 6 створено:

| Issue | Зміст |
|---|---|
| `#28` | Python-проєкт та віртуальне середовище |
| `#29` | CSV loader, валідація та DataError |
| `#30` | Аналіз даних, колекції та comprehensions |
| `#31` | Формування текстового звіту та CLI |
| `#32` | Pytest, Ruff та пакетування wheel |
| `#33` | Потокова обробка великого CSV |
| `#34` | CI, документація та реліз |

У фінальному Pull Request:

```text
Closes #28
Closes #29
Closes #30
Closes #31
Closes #32
Closes #33
Closes #34
```

Pull Request #35 успішно об'єднано з `main`.

Усі 7 Issues закрито.

## Підсумок

У Lab 6:

- створено Python-проєкт та віртуальне середовище;
- реалізовано читання та валідацію CSV;
- використано explicit UTF-8;
- використано списки, словники та множини;
- реалізовано comprehensions;
- використано типові параметри, `*args` та `**kwargs`;
- створено власний `DataError`;
- реалізовано аналіз комунальних показників;
- створено детермінований текстовий звіт;
- реалізовано CLI та коди завершення;
- реалізовано потоковий аналіз CSV ⭐;
- виконано benchmark на 100 000 записів ⭐;
- зменшено пікове використання пам'яті на 97.75% ⭐;
- додано 74 автоматизовані тести;
- pytest та Ruff проходять успішно;
- реалізовано пакетування wheel;
- CI налаштовано на Ubuntu, Windows і macOS;
- опубліковано 3 wheel artifacts;
- збережено функціональність Lab 1–5;
- виконано всі три рівні складності;
- виконано додаткове завдання із зірочкою;
- версію встановлено `6.0.0`.

Фінальний Git-тег після завершення роботи:

```text
v6.0.0
```