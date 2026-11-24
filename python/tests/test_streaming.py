"""Тести потокового аналізу комунальних показників."""

import pytest

from kzp_lab06.benchmark import results_match
from kzp_lab06.errors import DataError
from kzp_lab06.loader import iter_records
from kzp_lab06.streaming import analyze_in_memory, analyze_streaming

HEADER = "meter,date,value,tariff\n"


@pytest.fixture
def csv_path(tmp_path):
    """Створює контрольний CSV із повторюваними лічильниками."""
    path = tmp_path / "input.csv"

    path.write_text(
        HEADER
        + "Meter-1,2026-11-01,100,4.32\n"
        + "Meter-2,2026-11-02,50,2.5\n"
        + "Meter-1,2026-11-03,75,4.32\n"
        + '"Meter, ""night""\nreserve",2026-11-04,100,5.1\n',
        encoding="utf-8",
    )

    return path


@pytest.mark.parametrize("batch_size", [1, 2, 4, 1024])
def test_streaming_matches_baseline(csv_path, batch_size):
    """Перевіряє еквівалентність для різних розмірів порцій."""
    baseline = analyze_in_memory(csv_path)
    streaming = analyze_streaming(
        csv_path,
        batch_size=batch_size,
    )

    assert results_match(baseline, streaming)
    assert streaming["count"] == 4
    assert streaming["total_value"] == pytest.approx(325.0)
    assert streaming["total_cost"] == pytest.approx(1391.0)
    assert len(streaming["meters"]) == 3
    assert streaming["maximum"]["meter"] == "Meter-1"
    assert streaming["filtered"]["count"] == 2


def test_streaming_empty_dataset(tmp_path):
    """Перевіряє порожній коректний набір."""
    path = tmp_path / "empty.csv"
    path.write_text(HEADER, encoding="utf-8")

    result = analyze_streaming(path)

    assert results_match(analyze_in_memory(path), result)
    assert result["count"] == 0
    assert result["total_value"] == 0.0
    assert result["total_cost"] == 0.0
    assert result["maximum"] is None
    assert result["totals"] == {}
    assert result["meters"] == []


def test_streaming_no_matches(csv_path):
    """Перевіряє порожній результат відбору."""
    result = analyze_streaming(
        csv_path,
        threshold=1000.0,
    )

    assert result["filtered"] == {
        "count": 0,
        "value": 0.0,
        "cost": 0.0,
    }


@pytest.mark.parametrize("batch_size", [0, -1])
def test_invalid_batch_size(csv_path, batch_size):
    """Перевіряє недопустимий розмір порції."""
    with pytest.raises(ValueError, match="Розмір порції"):
        analyze_streaming(csv_path, batch_size=batch_size)


def test_streaming_invalid_record(tmp_path):
    """Перевіряє помилку під час потокового читання."""
    path = tmp_path / "invalid.csv"

    path.write_text(
        HEADER + "Meter-1,2026-11-01,100,4.32\n" + "Meter-2,2026-11-02,abc,2.5\n",
        encoding="utf-8",
    )

    with pytest.raises(DataError, match="Рядок 3: поле value"):
        analyze_streaming(path, batch_size=1)


def test_generator_lazy_loading(tmp_path):
    """Перевіряє ліниве читання записів генератором."""
    path = tmp_path / "input.csv"

    path.write_text(
        HEADER + "Meter-1,2026-11-01,100,4.32\n" + "Meter-2,2026-11-02,abc,2.5\n",
        encoding="utf-8",
    )

    records = iter_records(path)

    first = next(records)

    assert first["meter"] == "Meter-1"
    assert first["value"] == 100.0

    with pytest.raises(DataError, match="value"):
        next(records)
