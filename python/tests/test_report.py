"""Тести формування та запису текстового звіту."""

from datetime import date

from kzp_lab06.report import display_meter, format_report, write_report


def test_format_report():
    """Перевіряє повний текст контрольного звіту."""
    records = [
        {
            "meter": "Водомір-1",
            "date": date(2026, 11, 1),
            "value": 100.0,
            "tariff": 2.5,
        }
    ]

    expected = (
        "ЗВІТ КОМУНАЛЬНИХ ПОКАЗНИКІВ\n"
        "Лабораторна робота №6. Варіант №9\n"
        "\n"
        "Кількість записів: 1\n"
        "Кількість унікальних лічильників: 1\n"
        "Сумарне споживання: 100.00\n"
        "Загальна вартість: 250.00 грн\n"
        "\n"
        "НАЙБІЛЬШИЙ ПОКАЗНИК\n"
        "Лічильник: Водомір-1\n"
        "Дата: 2026-11-01\n"
        "Значення: 100.00\n"
        "\n"
        "СУМАРНЕ СПОЖИВАННЯ ЗА ЛІЧИЛЬНИКАМИ\n"
        "Водомір-1: 100.00\n"
        "\n"
        "УНІКАЛЬНІ ЛІЧИЛЬНИКИ\n"
        "Водомір-1\n"
        "\n"
        "ВІДБІР ЗАПИСІВ: value > 75.00\n"
        "Кількість відібраних записів: 1\n"
        "Сумарне споживання: 100.00\n"
        "Вартість споживання: 250.00 грн\n"
    )

    assert format_report(records) == expected


def test_empty_report():
    """Перевіряє звіт для порожнього набору."""
    report = format_report([])

    assert "Кількість записів: 0" in report
    assert "Кількість унікальних лічильників: 0" in report
    assert "Загальна вартість: 0.00 грн" in report
    assert "Дані відсутні" in report
    assert "Кількість відібраних записів: 0" in report


def test_display_meter():
    """Перевіряє відображення спеціальних символів."""
    meter = 'Лічильник, "нічний"\nрезервний'

    assert display_meter(meter) == r'Лічильник, "нічний"\nрезервний'
    assert display_meter("A\\B") == r"A\\B"


def test_write_report(tmp_path):
    """Перевіряє створення каталогу, UTF-8 та LF."""
    path = tmp_path / "reports" / "summary.txt"
    text = "Комунальні показники\nЗвіт сформовано\n"

    write_report(path, text)

    assert path.exists()
    assert path.read_text(encoding="utf-8") == text
    assert path.read_bytes() == text.encode("utf-8")
