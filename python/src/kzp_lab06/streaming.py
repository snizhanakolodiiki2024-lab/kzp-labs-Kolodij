"""Порівняння базового та потокового аналізу CSV."""

from contextlib import closing
from itertools import islice
from pathlib import Path

from .analysis import (
    DEFAULT_THRESHOLD,
    max_reading,
    sum_by_meter,
    summarize,
    total_cost,
    unique_meters,
)
from .loader import iter_records, load_records


def analyze_in_memory(
    path: Path,
    threshold: float = DEFAULT_THRESHOLD,
) -> dict[str, object]:
    """Аналізує CSV із завантаженням усіх записів у пам'ять."""
    records = load_records(path)
    totals = sum_by_meter(records)

    return {
        "count": len(records),
        "total_value": sum(totals.values()),
        "total_cost": total_cost(records),
        "maximum": max_reading(records),
        "totals": totals,
        "meters": sorted(unique_meters(records)),
        "filtered": summarize(
            records,
            "value",
            "cost",
            threshold=threshold,
        ),
    }


def analyze_streaming(
    path: Path,
    threshold: float = DEFAULT_THRESHOLD,
    batch_size: int = 1024,
) -> dict[str, object]:
    """Аналізує CSV обмеженими порціями записів."""
    if batch_size <= 0:
        raise ValueError("Розмір порції має бути додатним")

    count = 0
    cost = 0.0
    totals: dict[str, float] = {}
    maximum: dict[str, object] | None = None

    filtered_count = 0
    filtered_value = 0.0
    filtered_cost = 0.0

    with closing(iter_records(path)) as records:
        while batch := list(islice(records, batch_size)):
            for record in batch:
                meter = str(record["meter"])
                value = float(record["value"])
                tariff = float(record["tariff"])

                record_cost = value * tariff

                count += 1
                cost += record_cost

                totals[meter] = totals.get(meter, 0.0) + value

                if maximum is None or value > float(maximum["value"]):
                    maximum = record

                if value > threshold:
                    filtered_count += 1
                    filtered_value += value
                    filtered_cost += record_cost

    totals = dict(sorted(totals.items()))

    return {
        "count": count,
        "total_value": sum(totals.values()),
        "total_cost": cost,
        "maximum": maximum,
        "totals": totals,
        "meters": sorted(totals),
        "filtered": {
            "count": filtered_count,
            "value": filtered_value,
            "cost": filtered_cost,
        },
    }
