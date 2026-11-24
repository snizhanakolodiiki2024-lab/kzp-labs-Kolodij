"""Аналіз комунальних показників засобами Python."""

DEFAULT_THRESHOLD = 75.0


def filter_above_threshold(
    records: list[dict[str, object]],
    threshold: float = DEFAULT_THRESHOLD,
) -> list[dict[str, object]]:
    """Повертає записи зі споживанням понад заданий поріг."""
    return [record for record in records if float(record["value"]) > threshold]


def sum_by_meter(
    records: list[dict[str, object]],
) -> dict[str, float]:
    """Обчислює сумарне споживання за кожним лічильником."""
    totals: dict[str, float] = {}

    for record in records:
        meter = str(record["meter"])
        value = float(record["value"])
        totals[meter] = totals.get(meter, 0.0) + value

    return dict(sorted(totals.items()))


def unique_meters(
    records: list[dict[str, object]],
) -> set[str]:
    """Повертає множину унікальних лічильників."""
    return {str(record["meter"]) for record in records}


def total_cost(
    records: list[dict[str, object]],
) -> float:
    """Обчислює загальну вартість споживання."""
    return sum(
        (float(record["value"]) * float(record["tariff"]) for record in records),
        0.0,
    )


def max_reading(
    records: list[dict[str, object]],
) -> dict[str, object] | None:
    """Повертає запис із найбільшим значенням споживання."""
    return max(
        records,
        key=lambda record: float(record["value"]),
        default=None,
    )


def summarize(
    records: list[dict[str, object]],
    *metrics: str,
    threshold: float = DEFAULT_THRESHOLD,
    **labels: str,
) -> dict[str, object]:
    """Обчислює вибрані показники після відбору записів."""
    allowed_metrics = {"value", "cost"}

    for metric in metrics:
        if metric not in allowed_metrics:
            raise ValueError(f"Невідома метрика: {metric}")

    selected = filter_above_threshold(records, threshold)

    result: dict[str, object] = {"count": len(selected)}

    for metric in metrics:
        label = labels.get(metric, metric)

        if label in result:
            raise ValueError(f"Повторювана назва показника: {label}")

        if metric == "value":
            result[label] = sum(
                (float(record["value"]) for record in selected),
                0.0,
            )
        elif metric == "cost":
            result[label] = total_cost(selected)

    return result
