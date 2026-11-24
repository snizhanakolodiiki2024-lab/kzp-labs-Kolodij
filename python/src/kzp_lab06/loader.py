"""Завантаження та валідація комунальних показників із CSV."""

import csv
import math
from datetime import date
from pathlib import Path

from .errors import DataError

REQUIRED_FIELDS = {"meter", "date", "value", "tariff"}


def parse_text(
    row: dict[str, str],
    field: str,
    row_number: int,
) -> str:
    """Перевіряє та нормалізує текстове поле."""
    value = row.get(field)

    if value is None or not value.strip():
        raise DataError(f"Рядок {row_number}: поле {field} порожнє")

    return value.strip().replace("\r\n", "\n").replace("\r", "\n")


def parse_number(
    row: dict[str, str],
    field: str,
    row_number: int,
) -> float:
    """Перетворює поле на невід'ємне скінченне число."""
    text = parse_text(row, field, row_number)

    try:
        value = float(text)
    except ValueError as error:
        raise DataError(f"Рядок {row_number}: поле {field} має бути числом") from error

    if not math.isfinite(value) or value < 0:
        raise DataError(f"Рядок {row_number}: поле {field} має бути невід'ємним скінченним числом")

    return value


def parse_date(
    row: dict[str, str],
    row_number: int,
) -> date:
    """Перетворює текстову дату на об'єкт date."""
    text = parse_text(row, "date", row_number)

    try:
        result = date.fromisoformat(text)

        if result.isoformat() != text:
            raise ValueError("Очікується формат YYYY-MM-DD")

        return result

    except ValueError as error:
        raise DataError(f"Рядок {row_number}: некоректне поле date") from error


def load_records(path: Path) -> list[dict[str, object]]:
    """Читає CSV та повертає список перевірених записів."""
    records = []

    try:
        with path.open("r", encoding="utf-8", newline="") as source:
            reader = csv.DictReader(source, strict=True)

            try:
                fields = reader.fieldnames

                if (
                    fields is None
                    or len(fields) != len(REQUIRED_FIELDS)
                    or set(fields) != REQUIRED_FIELDS
                ):
                    raise DataError("Некоректний заголовок CSV")

                for row_number, row in enumerate(reader, start=2):
                    if None in row:
                        raise DataError(f"Рядок {row_number}: зайві поля")

                    records.append(
                        {
                            "meter": parse_text(row, "meter", row_number),
                            "date": parse_date(row, row_number),
                            "value": parse_number(row, "value", row_number),
                            "tariff": parse_number(row, "tariff", row_number),
                        }
                    )

            except csv.Error as error:
                raise DataError(f"Рядок {reader.line_num}: пошкоджений CSV") from error

    except (OSError, UnicodeError) as error:
        raise DataError(f"Не вдалося прочитати файл: {path}") from error

    return records
