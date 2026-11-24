"""Інтерфейс командного рядка для лабораторної роботи №6."""

import argparse
import math
import sys
from pathlib import Path

from .analysis import DEFAULT_THRESHOLD
from .errors import DataError
from .loader import load_records
from .report import format_report, write_report


def configure_encoding() -> None:
    """Налаштовує UTF-8 для стандартних потоків."""
    for stream in (sys.stdout, sys.stderr):
        try:
            stream.reconfigure(encoding="utf-8")
        except (AttributeError, ValueError):
            pass


def main(argv: list[str] | None = None) -> int:
    """Обробляє аргументи CLI та формує текстовий звіт."""
    configure_encoding()

    parser = argparse.ArgumentParser(
        description="Обробка комунальних показників. Lab 6, варіант 9."
    )

    parser.add_argument(
        "input_csv",
        type=Path,
        help="Шлях до вхідного CSV-файла",
    )

    parser.add_argument(
        "output_report",
        type=Path,
        help="Шлях до вихідного текстового звіту",
    )

    parser.add_argument(
        "--threshold",
        type=float,
        default=DEFAULT_THRESHOLD,
        help="Поріг відбору записів",
    )

    args = parser.parse_args(argv)

    try:
        if not math.isfinite(args.threshold) or args.threshold < 0:
            raise DataError("Поріг має бути невід'ємним скінченним числом")

        if args.input_csv.resolve() == args.output_report.resolve():
            raise DataError("Вхідний та вихідний файли мають бути різними")

        if (
            args.input_csv.exists()
            and args.output_report.exists()
            and args.input_csv.samefile(args.output_report)
        ):
            raise DataError("Вхідний та вихідний файли мають бути різними")

        records = load_records(args.input_csv)
        report = format_report(records, args.threshold)

        write_report(args.output_report, report)

    except DataError as error:
        print(f"Помилка даних: {error}", file=sys.stderr)
        return 2

    except OSError as error:
        print(f"Помилка файлової системи: {error}", file=sys.stderr)
        return 2

    print(report, end="")
    print(f"\nЗвіт збережено: {args.output_report}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
