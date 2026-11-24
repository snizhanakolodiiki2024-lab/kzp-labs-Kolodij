"""Тести інтерфейсу командного рядка."""

import subprocess
import sys

import pytest

HEADER = "meter,date,value,tariff\n"
VALID_ROW = "Водомір-1,2026-11-01,100,2.5\n"


def create_csv(tmp_path, content=HEADER + VALID_ROW):
    """Створює тимчасовий вхідний CSV."""
    path = tmp_path / "input.csv"
    path.write_text(content, encoding="utf-8")
    return path


def run_cli(*args):
    """Запускає CLI в окремому процесі Python."""
    return subprocess.run(
        [
            sys.executable,
            "-m",
            "kzp_lab06.cli",
            *map(str, args),
        ],
        capture_output=True,
        text=True,
        encoding="utf-8",
        check=False,
    )


def test_cli_success(tmp_path):
    """Перевіряє успішний запуск та створення звіту."""
    source = create_csv(tmp_path)
    output = tmp_path / "summary.txt"

    result = run_cli(source, output)

    assert result.returncode == 0
    assert result.stderr == ""
    assert output.exists()
    assert "Загальна вартість: 250.00 грн" in result.stdout
    assert "Звіт збережено:" in result.stdout
    assert source.read_text(encoding="utf-8") == HEADER + VALID_ROW


def test_cli_custom_threshold(tmp_path):
    """Перевіряє передавання власного порогу."""
    source = create_csv(tmp_path)
    output = tmp_path / "summary.txt"

    result = run_cli(source, output, "--threshold", "150")

    assert result.returncode == 0
    assert "value > 150.00" in result.stdout
    assert "Кількість відібраних записів: 0" in result.stdout


def test_cli_invalid_csv(tmp_path):
    """Перевіряє помилку вхідних даних."""
    source = create_csv(
        tmp_path,
        HEADER + "Водомір-1,2026-11-01,abc,2.5\n",
    )
    output = tmp_path / "summary.txt"

    result = run_cli(source, output)

    assert result.returncode == 2
    assert "Помилка даних:" in result.stderr
    assert "Рядок 2" in result.stderr
    assert "value" in result.stderr
    assert "Traceback" not in result.stderr
    assert not output.exists()


def test_cli_missing_file(tmp_path):
    """Перевіряє запуск із відсутнім CSV."""
    source = tmp_path / "missing.csv"
    output = tmp_path / "summary.txt"

    result = run_cli(source, output)

    assert result.returncode == 2
    assert "Помилка даних:" in result.stderr
    assert not output.exists()


def test_cli_same_input_output(tmp_path):
    """Перевіряє захист від перезапису вхідного CSV."""
    source = create_csv(tmp_path)
    original = source.read_bytes()

    result = run_cli(source, source)

    assert result.returncode == 2
    assert "мають бути різними" in result.stderr
    assert source.read_bytes() == original


@pytest.mark.parametrize("threshold", ["-1", "NaN", "Infinity"])
def test_cli_invalid_threshold(tmp_path, threshold):
    """Перевіряє некоректні значення порогу."""
    source = create_csv(tmp_path)
    output = tmp_path / "summary.txt"

    result = run_cli(source, output, f"--threshold={threshold}")

    assert result.returncode == 2
    assert "Поріг" in result.stderr
    assert not output.exists()


def test_cli_output_error(tmp_path):
    """Перевіряє помилку запису вихідного файла."""
    source = create_csv(tmp_path)
    output = tmp_path / "reports"
    output.mkdir()

    result = run_cli(source, output)

    assert result.returncode == 2
    assert "Помилка файлової системи:" in result.stderr


def test_cli_help():
    """Перевіряє довідку CLI."""
    result = run_cli("--help")

    assert result.returncode == 0
    assert "input_csv" in result.stdout
    assert "output_report" in result.stdout
    assert "--threshold" in result.stdout


def test_cli_import():
    """Перевіряє відсутність запуску CLI під час імпорту."""
    result = subprocess.run(
        [sys.executable, "-c", "import kzp_lab06.cli"],
        capture_output=True,
        text=True,
        encoding="utf-8",
        check=False,
    )

    assert result.returncode == 0
    assert result.stdout == ""
    assert result.stderr == ""
