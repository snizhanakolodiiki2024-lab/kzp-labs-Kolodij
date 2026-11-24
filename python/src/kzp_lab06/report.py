"""Формування та запис звіту комунальних показників."""

from pathlib import Path

from .analysis import (
    DEFAULT_THRESHOLD,
    max_reading,
    sum_by_meter,
    summarize,
    total_cost,
    unique_meters,
)


def display_meter(meter: str) -> str:
    """Повертає назву лічильника в одному рядку звіту."""
    return meter.replace("\\", "\\\\").replace("\n", "\\n")


def format_report(
    records: list[dict[str, object]],
    threshold: float = DEFAULT_THRESHOLD,
) -> str:
    """Формує детермінований текстовий звіт."""
    totals = sum_by_meter(records)
    meters = unique_meters(records)
    maximum = max_reading(records)

    summary = summarize(
        records,
        "value",
        "cost",
        threshold=threshold,
    )

    lines = [
        "ЗВІТ КОМУНАЛЬНИХ ПОКАЗНИКІВ",
        "Лабораторна робота №6. Варіант №9",
        "",
        f"Кількість записів: {len(records)}",
        f"Кількість унікальних лічильників: {len(meters)}",
        f"Сумарне споживання: {sum(totals.values()):.2f}",
        f"Загальна вартість: {total_cost(records):.2f} грн",
        "",
        "НАЙБІЛЬШИЙ ПОКАЗНИК",
    ]

    if maximum is None:
        lines.append("Дані відсутні")
    else:
        lines.extend(
            [
                f"Лічильник: {display_meter(str(maximum['meter']))}",
                f"Дата: {maximum['date']}",
                f"Значення: {float(maximum['value']):.2f}",
            ]
        )

    lines.extend(["", "СУМАРНЕ СПОЖИВАННЯ ЗА ЛІЧИЛЬНИКАМИ"])

    for meter, value in sorted(totals.items()):
        lines.append(f"{display_meter(meter)}: {value:.2f}")

    if not totals:
        lines.append("Дані відсутні")

    lines.extend(["", "УНІКАЛЬНІ ЛІЧИЛЬНИКИ"])

    for meter in sorted(meters):
        lines.append(display_meter(meter))

    if not meters:
        lines.append("Дані відсутні")

    lines.extend(
        [
            "",
            f"ВІДБІР ЗАПИСІВ: value > {threshold:.2f}",
            f"Кількість відібраних записів: {summary['count']}",
            f"Сумарне споживання: {float(summary['value']):.2f}",
            f"Вартість споживання: {float(summary['cost']):.2f} грн",
        ]
    )

    return "\n".join(lines) + "\n"


def write_report(path: Path, text: str) -> None:
    """Створює вихідний каталог і записує звіт у UTF-8."""
    path.parent.mkdir(parents=True, exist_ok=True)

    with path.open("w", encoding="utf-8", newline="\n") as target:
        target.write(text)
