package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Тести для консольної програми лабораторної роботи №1.
 */
class MainTest {

    /**
     * Тимчасовий каталог, який JUnit автоматично створює для тесту.
     */
    @TempDir
    Path tempDir;

    /**
     * Перевіряє обробку коректних і пошкоджених записів.
     *
     * @throws Exception якщо виникла помилка роботи з тестовими файлами
     */
    @Test
    void processesValidAndInvalidRecords() throws Exception {
        Path input = tempDir.resolve("input.csv");
        Path output = tempDir.resolve("report.txt");

        String inputData = String.join(
                System.lineSeparator(),
                "Електролічильник-1;2026-09-01;125.50;4.32",
                "Водомір-1;2026-09-02;12.75;35.20",
                "Електролічильник-2;2026-09-03;48.00;4.32",
                "Теплолічильник-1;2026-09-04;-10.00;8.50",
                "Водомір-2;2026-09-05;abc;35.20");

        Files.writeString(
                input,
                inputData,
                StandardCharsets.UTF_8);

        Main.main(new String[] {
                "--input", input.toString(),
                "--output", output.toString()
        });

        String actualReport = Files.readString(
                output,
                StandardCharsets.UTF_8);

        String expectedReport = String.format(
                Locale.ROOT,
                "Результати:%n"
                        + "Кількість коректних записів: 3%n"
                        + "Сумарне споживання: 186.25%n"
                        + "Загальна вартість: 1198.32%n"
                        + "Найбільше споживання: 125.50%n");

        assertEquals(expectedReport, actualReport);
    }
    /**
 * Перевіряє порожній рядок, порожнє поле
 * та неправильну кількість полів.
 *
 * @throws Exception якщо виникла помилка роботи з файлами
 */
@Test
void ignoresMalformedRecords() throws Exception {
    Path input = tempDir.resolve("malformed.csv");
    Path output = tempDir.resolve("malformed-report.txt");

    String inputData = String.join(
            System.lineSeparator(),
            "Водомір-1;2026-09-01;10.00;2.00",
            "",
            "Водомір-2;;15.00;2.00",
            "Газовий-лічильник;2026-09-03;20.00");

    Files.writeString(
            input,
            inputData,
            StandardCharsets.UTF_8);

    Main.main(new String[] {
            "--input", input.toString(),
            "--output", output.toString()
    });

    String actualReport = Files.readString(
            output,
            StandardCharsets.UTF_8);

    String expectedReport = String.format(
            Locale.ROOT,
            "Результати:%n"
                    + "Кількість коректних записів: 1%n"
                    + "Сумарне споживання: 10.00%n"
                    + "Загальна вартість: 20.00%n"
                    + "Найбільше споживання: 10.00%n");

    assertEquals(expectedReport, actualReport);
}
/**
 * Перевіряє поведінку програми,
 * якщо у файлі немає жодного коректного запису.
 *
 * @throws Exception якщо виникла помилка роботи з файлами
 */
@Test
void doesNotCreateReportWhenNoValidRecordsExist() throws Exception {
    Path input = tempDir.resolve("invalid.csv");
    Path output = tempDir.resolve("invalid-report.txt");

    String inputData = String.join(
            System.lineSeparator(),
            "",
            "Водомір-1;;10.00;2.00",
            "Водомір-2;2026-09-02;abc;2.00",
            "Електролічильник-1;2026-09-03;-5.00;4.32",
            "Газовий-лічильник;2026-09-04;20.00");

    Files.writeString(
            input,
            inputData,
            StandardCharsets.UTF_8);

    Main.main(new String[] {
            "--input", input.toString(),
            "--output", output.toString()
    });

    assertFalse(Files.exists(output));
}
@Test
void printsVersion() {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    try {
        System.setOut(
                new PrintStream(
                        output,
                        true,
                        StandardCharsets.UTF_8));

        Main.main(new String[]{"--version"});
    } finally {
        System.setOut(originalOut);
    }

    assertEquals(
            "Комунальні показники 1.0.0"
                    + System.lineSeparator(),
            output.toString(StandardCharsets.UTF_8));
}
}