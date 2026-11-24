"""Тести завантаження та валідації CSV."""

from datetime import date

import pytest

from kzp_lab06.errors import DataError
from kzp_lab06.loader import load_records

HEADER = "meter,date,value,tariff\n"


def create_csv(tmp_path, content):
    """Створює тимчасовий CSV-файл для тестування."""
    path = tmp_path / "input.csv"
    path.write_text(content, encoding="utf-8")
    return path


def test_load_valid_record(tmp_path):
    """Перевіряє читання коректного запису та типи полів."""
    path = create_csv(
        tmp_path,
        HEADER + "Електролічильник-1,2026-11-01,125.5,4.32\n",
    )

    records = load_records(path)

    assert records == [
        {
            "meter": "Електролічильник-1",
            "date": date(2026, 11, 1),
            "value": 125.5,
            "tariff": 4.32,
        }
    ]


def test_empty_dataset(tmp_path):
    """Перевіряє CSV із заголовком без записів."""
    path = create_csv(tmp_path, HEADER)

    assert load_records(path) == []


def test_empty_file(tmp_path):
    """Перевіряє повністю порожній файл."""
    path = create_csv(tmp_path, "")

    with pytest.raises(DataError, match="заголовок"):
        load_records(path)


def test_zero_values(tmp_path):
    """Перевіряє допустимі нульові значення."""
    path = create_csv(
        tmp_path,
        HEADER + "Водомір-1,2026-11-01,0,0\n",
    )

    record = load_records(path)[0]

    assert record["value"] == 0.0
    assert record["tariff"] == 0.0


@pytest.mark.parametrize(
    "value",
    ["abc", "-10", "NaN", "Infinity", "-Infinity"],
)
def test_invalid_value(tmp_path, value):
    """Перевіряє некоректні значення споживання."""
    path = create_csv(
        tmp_path,
        HEADER + f"Водомір-1,2026-11-01,{value},4.32\n",
    )

    with pytest.raises(DataError, match="Рядок 2: поле value"):
        load_records(path)


@pytest.mark.parametrize("tariff", ["abc", "-1", "NaN"])
def test_invalid_tariff(tmp_path, tariff):
    """Перевіряє некоректні тарифи."""
    path = create_csv(
        tmp_path,
        HEADER + f"Водомір-1,2026-11-01,10,{tariff}\n",
    )

    with pytest.raises(DataError, match="Рядок 2: поле tariff"):
        load_records(path)


def test_invalid_date(tmp_path):
    """Перевіряє некоректну календарну дату."""
    path = create_csv(
        tmp_path,
        HEADER + "Водомір-1,2026-02-30,10,4.32\n",
    )

    with pytest.raises(DataError, match="date"):
        load_records(path)


def test_missing_column(tmp_path):
    """Перевіряє відсутній обов'язковий стовпець."""
    path = create_csv(
        tmp_path,
        "meter,date,value\nВодомір-1,2026-11-01,10\n",
    )

    with pytest.raises(DataError, match="заголовок"):
        load_records(path)


def test_missing_value(tmp_path):
    """Перевіряє відсутнє значення в рядку."""
    path = create_csv(
        tmp_path,
        HEADER + "Водомір-1,2026-11-01,10\n",
    )

    with pytest.raises(DataError, match="tariff"):
        load_records(path)


def test_empty_meter(tmp_path):
    """Перевіряє порожню назву лічильника."""
    path = create_csv(
        tmp_path,
        HEADER + ",2026-11-01,10,4.32\n",
    )

    with pytest.raises(DataError, match="meter"):
        load_records(path)


def test_extra_field(tmp_path):
    """Перевіряє зайве поле CSV."""
    path = create_csv(
        tmp_path,
        HEADER + "Водомір-1,2026-11-01,10,4.32,extra\n",
    )

    with pytest.raises(DataError, match="зайві поля"):
        load_records(path)


def test_duplicate_records(tmp_path):
    """Перевіряє збереження повторюваних записів."""
    row = "Водомір-1,2026-11-01,10,4.32\n"
    path = create_csv(tmp_path, HEADER + row + row)

    records = load_records(path)

    assert len(records) == 2
    assert records[0] == records[1]


def test_quoted_multiline_field(tmp_path):
    """Перевіряє кому, лапки та перенесення рядка."""
    path = create_csv(
        tmp_path,
        HEADER + '"Лічильник, ""нічний""\nрезервний",' + "2026-11-05,42.75,5.1\n",
    )

    record = load_records(path)[0]

    assert record["meter"] == 'Лічильник, "нічний"\nрезервний'


def test_malformed_csv(tmp_path):
    """Перевіряє пошкоджений CSV та збереження причини."""
    path = create_csv(
        tmp_path,
        HEADER + '"Незакриті лапки,2026-11-01,10,4.32\n',
    )

    with pytest.raises(DataError, match="пошкоджений CSV") as error:
        load_records(path)

    assert error.value.__cause__ is not None


def test_error_cause(tmp_path):
    """Перевіряє збереження першопричини помилки числа."""
    path = create_csv(
        tmp_path,
        HEADER + "Водомір-1,2026-11-01,abc,4.32\n",
    )

    with pytest.raises(DataError) as error:
        load_records(path)

    assert isinstance(error.value.__cause__, ValueError)


def test_missing_file(tmp_path):
    """Перевіряє помилку файлової системи."""
    path = tmp_path / "missing.csv"

    with pytest.raises(DataError) as error:
        load_records(path)

    assert isinstance(error.value.__cause__, FileNotFoundError)
