"""Тести аналізу комунальних показників."""

from datetime import date

import pytest

from kzp_lab06.analysis import (
    filter_above_threshold,
    max_reading,
    sum_by_meter,
    summarize,
    total_cost,
    unique_meters,
)


@pytest.fixture
def records():
    """Повертає контрольний набір комунальних показників."""
    return [
        {
            "meter": "Електролічильник-1",
            "date": date(2026, 11, 1),
            "value": 125.5,
            "tariff": 4.32,
        },
        {
            "meter": "Водомір-1",
            "date": date(2026, 11, 2),
            "value": 35.25,
            "tariff": 35.2,
        },
        {
            "meter": "Електролічильник-1",
            "date": date(2026, 11, 6),
            "value": 75.0,
            "tariff": 4.32,
        },
    ]


def test_filter_default_threshold(records):
    """Перевіряє відбір за типовим порогом."""
    assert filter_above_threshold(records) == [records[0]]


def test_filter_custom_threshold(records):
    """Перевіряє відбір за заданим порогом."""
    assert filter_above_threshold(records, 50.0) == [
        records[0],
        records[2],
    ]


def test_filter_boundary(records):
    """Перевіряє строгу умову value > threshold."""
    result = filter_above_threshold(records, 75.0)

    assert all(record["value"] > 75.0 for record in result)
    assert records[2] not in result


def test_filter_no_matches(records):
    """Перевіряє порожній результат відбору."""
    assert filter_above_threshold(records, 1000.0) == []


def test_sum_by_meter(records):
    """Перевіряє агрегування повторюваних лічильників."""
    assert sum_by_meter(records) == {
        "Водомір-1": 35.25,
        "Електролічильник-1": 200.5,
    }


def test_sorted_meter_keys(records):
    """Перевіряє детермінований порядок ключів."""
    result = sum_by_meter(records)

    assert list(result) == sorted(result)


def test_unique_meters(records):
    """Перевіряє усунення повторів у множині."""
    assert unique_meters(records) == {
        "Електролічильник-1",
        "Водомір-1",
    }


def test_total_cost(records):
    """Перевіряє загальну вартість споживання."""
    assert total_cost(records) == pytest.approx(2106.96)


def test_max_reading(records):
    """Перевіряє пошук найбільшого показника."""
    assert max_reading(records) == records[0]


def test_max_reading_tie(records):
    """Перевіряє вибір першого запису при однакових значеннях."""
    first = records[0]
    second = {**first, "meter": "Інший лічильник"}

    assert max_reading([first, second]) == first


def test_empty_records():
    """Перевіряє всі основні функції на порожньому наборі."""
    assert filter_above_threshold([]) == []
    assert sum_by_meter([]) == {}
    assert unique_meters([]) == set()
    assert total_cost([]) == 0.0
    assert max_reading([]) is None


def test_summarize_default_threshold(records):
    """Перевіряє типовий параметр і довільні аргументи."""
    result = summarize(records, "value", "cost")

    assert result["count"] == 1
    assert result["value"] == pytest.approx(125.5)
    assert result["cost"] == pytest.approx(542.16)


def test_summarize_custom_labels(records):
    """Перевіряє *args, **kwargs та заданий поріг."""
    result = summarize(
        records,
        "value",
        "cost",
        threshold=50.0,
        value="Споживання",
        cost="Вартість",
    )

    assert result["count"] == 2
    assert result["Споживання"] == pytest.approx(200.5)
    assert result["Вартість"] == pytest.approx(866.16)


def test_summarize_empty_records():
    """Перевіряє підсумки порожнього набору."""
    assert summarize([], "value", "cost") == {
        "count": 0,
        "value": 0.0,
        "cost": 0.0,
    }


def test_summarize_no_matches(records):
    """Перевіряє підсумки після відбору без результатів."""
    assert summarize(
        records,
        "value",
        "cost",
        threshold=1000.0,
    ) == {
        "count": 0,
        "value": 0.0,
        "cost": 0.0,
    }


@pytest.mark.parametrize("data", [[], None])
def test_unknown_metric(records, data):
    """Перевіряє відхилення невідомої метрики."""
    selected = records if data is None else data

    with pytest.raises(ValueError, match="Невідома метрика"):
        summarize(selected, "unknown")


def test_duplicate_labels(records):
    """Перевіряє відхилення повторюваних назв показників."""
    with pytest.raises(ValueError, match="Повторювана назва"):
        summarize(
            records,
            "value",
            "cost",
            value="Показник",
            cost="Показник",
        )


def test_input_not_mutated(records):
    """Перевіряє відсутність зміни початкових записів."""
    original = [record.copy() for record in records]

    filter_above_threshold(records)
    sum_by_meter(records)
    unique_meters(records)
    total_cost(records)
    max_reading(records)
    summarize(records, "value", "cost")

    assert records == original
