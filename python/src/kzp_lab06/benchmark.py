"""Вимірювання пам'яті базового та потокового аналізу."""

import csv
import gc
import math
import tracemalloc
from collections.abc import Callable
from itertools import cycle, islice
from pathlib import Path
from time import perf_counter

from .loader import load_records
from .streaming import analyze_in_memory, analyze_streaming

RECORD_COUNT = 100_000
BATCH_SIZE = 1024


def generate_large_csv(
    source: Path,
    target: Path,
    count: int = RECORD_COUNT,
) -> None:
    """Створює великий CSV на основі контрольних записів."""
    records = load_records(source)

    if count < 0:
        raise ValueError("Кількість записів не може бути від'ємною")

    if count and not records:
        raise ValueError("Вхідний набір не може бути порожнім")

    target.parent.mkdir(parents=True, exist_ok=True)

    with target.open("w", encoding="utf-8", newline="") as output:
        writer = csv.DictWriter(
            output,
            fieldnames=["meter", "date", "value", "tariff"],
        )

        writer.writeheader()
        writer.writerows(islice(cycle(records), count))


def measure(
    operation: Callable[[], dict[str, object]],
) -> tuple[dict[str, object], int, float]:
    """Вимірює пікову пам'ять і час виконання операції."""
    gc.collect()
    tracemalloc.start()

    start = perf_counter()

    try:
        result = operation()
        elapsed = perf_counter() - start
        _, peak = tracemalloc.get_traced_memory()

        return result, peak, elapsed

    finally:
        tracemalloc.stop()


def results_match(left: object, right: object) -> bool:
    """Порівнює результати з урахуванням точності float."""
    if isinstance(left, dict) and isinstance(right, dict):
        return left.keys() == right.keys() and all(
            results_match(left[key], right[key]) for key in left
        )

    if isinstance(left, float) and isinstance(right, float):
        return math.isclose(
            left,
            right,
            rel_tol=1e-9,
            abs_tol=1e-9,
        )

    return left == right


def main() -> int:
    """Запускає порівняння двох підходів."""
    source = Path("data/input.csv")
    target = Path("reports/large-input.csv")

    generate_large_csv(source, target)

    baseline, baseline_peak, baseline_time = measure(lambda: analyze_in_memory(target))

    streaming, streaming_peak, streaming_time = measure(
        lambda: analyze_streaming(
            target,
            batch_size=BATCH_SIZE,
        )
    )

    equal = results_match(baseline, streaming)

    reduction = (1 - streaming_peak / baseline_peak) * 100

    ratio = baseline_peak / streaming_peak

    print("CSV MEMORY BENCHMARK")
    print("-" * 50)
    print(f"Records: {baseline['count']}")
    print(f"Batch size: {BATCH_SIZE}")
    print(f"CSV size: {target.stat().st_size / 1024**2:.2f} MiB")

    print("\nIN-MEMORY ANALYSIS")
    print(f"Peak memory: {baseline_peak / 1024**2:.3f} MiB")
    print(f"Execution time: {baseline_time:.3f} s")

    print("\nSTREAMING ANALYSIS")
    print(f"Peak memory: {streaming_peak / 1024**2:.3f} MiB")
    print(f"Execution time: {streaming_time:.3f} s")

    print("\nCOMPARISON")
    print(f"Results match: {equal}")
    print(f"Memory reduction: {reduction:.2f}%")
    print(f"Memory ratio: {ratio:.2f}x")

    print("\nCONTROL RESULTS")
    print(f"Total consumption: {baseline['total_value']:.2f}")
    print(f"Total cost: {baseline['total_cost']:.2f}")
    print(f"Unique meters: {len(baseline['meters'])}")

    return 0 if equal else 1


if __name__ == "__main__":
    raise SystemExit(main())
