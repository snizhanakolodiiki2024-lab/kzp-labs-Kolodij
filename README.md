# Кросплатформні засоби програмування

**Виконала:** Колодій С. М.  
**Група:** КІ-303  
**Варіант:** №9  
**Предметна область:** комунальні показники

## Структура репозиторію

Репозиторій містить два незалежні проєкти:

| Каталог | Мова | Лабораторні роботи |
|---|---|---|
| `java/` | Java 21 | №1–5 |
| `python/` | Python 3.12 | №6 і №7 |

Кожен проєкт має власний вихідний код, тести, вхідні дані, конфігурацію збірки та документацію.

## Java

- [README.md](java/README.md)
- [REPORT.md](java/REPORT.md)

Збірка та перевірка:

```powershell
cd java
.\mvnw.cmd -B verify
```

## Python

- [README.md](python/README.md)
- [REPORT.md](python/REPORT.md)

Налаштування та перевірка:

```powershell
cd python
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -e ".[dev]"
python -m pytest
ruff check .
ruff format --check .
python -m build
```

## GitHub Actions

Для кожного проєкту налаштовано незалежний workflow:

- [Java CI](.github/workflows/ci.yml)
- [Python CI](.github/workflows/python-ci.yml)

Перевірки виконуються на Ubuntu, Windows та macOS.

## Версії

- Java — `5.0.0`.
- Python — `6.0.0`.

Історію комітів, GitHub Issues, Pull Requests та попередні Git-теги збережено.