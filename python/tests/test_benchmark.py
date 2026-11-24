"""Тести генерації великих CSV та вимірювання пам'яті."""

import pytest

from kzp_lab06.benchmark import (
    generate_large_csv,
    measure,
    results_match,
)
from kzp_lab06.loader import load_records

HEADER = "meter,date,value,tariff\n"


@pytest.fixture
def source_csv(tmp_path):
    """Створює невеликий набір для генерації CSV."""
    path = tmp_path / "source.csv"

    path.write_text(
        HEADER + "Meter-1,2026-11-01,100,4.32\n" + "Meter-2,2026-11-02,50,2.5\n",
        encoding="utf-8",
    )

    return path


def test_generate_large_csv(source_csv, tmp_path):
    """Перевіряє кількість і порядок згенерованих записів."""
    target = tmp_path / "reports" / "large.csv"
    original = source_csv.read_bytes()

    generate_large_csv(source_csv, target, count=7)

    source_records = load_records(source_csv)
    generated = load_records(target)

    assert len(generated) == 7
    assert generated == [source_records[index % 2] for index in range(7)]
    assert source_csv.read_bytes() == original


def test_generate_zero_records(source_csv, tmp_path):
    """Перевіряє генерацію CSV лише із заголовком."""
    target = tmp_path / "empty.csv"

    generate_large_csv(source_csv, target, count=0)

    assert load_records(target) == []


def test_negative_record_count(source_csv, tmp_path):
    """Перевіряє відхилення від'ємної кількості записів."""
    target = tmp_path / "large.csv"

    with pytest.raises(ValueError, match="Кількість записів"):
        generate_large_csv(source_csv, target, count=-1)


def test_empty_source(tmp_path):
    """Перевіряє неможливість повторення порожнього набору."""
    source = tmp_path / "empty.csv"
    target = tmp_path / "large.csv"

    source.write_text(HEADER, encoding="utf-8")

    with pytest.raises(ValueError, match="порожнім"):
        generate_large_csv(source, target, count=10)


def test_measure():
    """Перевіряє отримання результату, пам'яті та часу."""
    result, peak, elapsed = measure(lambda: {"values": list(range(1000))})

    assert len(result["values"]) == 1000
    assert peak > 0
    assert elapsed >= 0.0


def test_results_match_float_precision():
    """Перевіряє допустиму похибку дробових чисел."""
    left = {"cost": 0.1 + 0.2}
    right = {"cost": 0.3}

    assert results_match(left, right)


def test_results_match_nested_difference():
    """Перевіряє виявлення різних вкладених результатів."""
    left = {"filtered": {"cost": 100.0}}
    right = {"filtered": {"cost": 200.0}}

    assert not results_match(left, right)


def test_results_match_different_keys():
    """Перевіряє відмінність ключів словників."""
    assert not results_match(
        {"value": 100.0},
        {"cost": 100.0},
    )
