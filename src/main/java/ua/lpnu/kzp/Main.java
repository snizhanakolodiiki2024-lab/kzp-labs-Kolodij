package ua.lpnu.kzp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Головний клас консольної програми лабораторної роботи №4.
 */
public final class Main {

    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_ARGUMENT_ERROR = 1;
    private static final int EXIT_IO_ERROR = 2;

    /**
     * Забороняє створення екземплярів службового класу.
     */
    private Main() {
    }

    /**
     * Точка входу до програми.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        int exitCode = run(args);

        if (exitCode != EXIT_SUCCESS) {
            System.exit(exitCode);
        }
    }

    /**
     * Виконує основну логіку програми та повертає код завершення.
     *
     * @param args аргументи командного рядка
     * @return код завершення програми
     */
    static int run(String[] args) {
        Path input = Path.of("data", "input.csv");
        Path output = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            if ("--help".equals(args[i])) {
                printHelp();
                return EXIT_SUCCESS;
            }

            if ("--version".equals(args[i])) {
                printVersion();
                return EXIT_SUCCESS;
            }

            if ("--input".equals(args[i])) {
                if (i + 1 >= args.length) {
                    System.out.printf(
                            "Помилка: після --input потрібно вказати шлях до файла%n"
                    );
                    return EXIT_ARGUMENT_ERROR;
                }

                input = Path.of(args[++i]);
                continue;
            }

            if ("--output".equals(args[i])) {
                if (i + 1 >= args.length) {
                    System.out.printf(
                            "Помилка: після --output потрібно вказати шлях до файла%n"
                    );
                    return EXIT_ARGUMENT_ERROR;
                }

                output = Path.of(args[++i]);
                continue;
            }

            System.out.printf(
                    "Помилка: невідомий аргумент %s%n",
                    args[i]
            );
            return EXIT_ARGUMENT_ERROR;
        }

        try {
            List<String> lines = readInput(input);

            System.out.printf(
                    "Прочитано рядків: %d%n",
                    lines.size()
            );

            String report = processLines(lines);

            if (report == null) {
                return EXIT_SUCCESS;
            }

            System.out.printf("%n%s", report);

            writeReport(output, report);

            System.out.printf(
                    "Звіт записано у файл: %s%n",
                    output
            );

            return EXIT_SUCCESS;
        } catch (IOException exception) {
            System.out.printf(
                    "Помилка роботи з файлом: %s%n",
                    exception.getMessage()
            );
            return EXIT_IO_ERROR;
        }
    }

    /**
     * Виводить довідку про параметри командного рядка.
     */
    private static void printHelp() {
        System.out.printf(
                "Використання: java -jar <файл.jar> "
                        + "[--help] [--version] "
                        + "[--input <файл>] [--output <файл>]%n"
        );
    }

    /**
     * Виводить версію програми.
     */
    private static void printVersion() {
        String version = getVersion();
        String buildNumber = System.getenv("CI_BUILD_NUMBER");

        if (buildNumber == null || buildNumber.isBlank()) {
            System.out.printf(
                    "Version %s%n",
                    version
            );
            return;
        }

        System.out.printf(
                "Version %s (build %s)%n",
                version,
                buildNumber
        );
    }

    /**
     * Повертає версію програми, яку Maven підставляє
     * у файл version.properties під час збирання.
     *
     * @return версія програми
     * @throws IllegalStateException якщо файл версії відсутній
     *         або його неможливо прочитати
     */
    private static String getVersion() {
        Properties properties = new Properties();

        try (InputStream input = Main.class
                .getClassLoader()
                .getResourceAsStream("version.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "version.properties not found"
                );
            }

            properties.load(input);

            String version = properties.getProperty("app.version");

            if (version == null || version.isBlank()) {
                throw new IllegalStateException(
                        "app.version is not defined"
                );
            }

            return version;
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to read application version",
                    exception
            );
        }
    }

    /**
     * Читає всі рядки вхідного файла у кодуванні UTF-8.
     *
     * @param input шлях до вхідного файла
     * @return список прочитаних рядків
     * @throws IOException якщо файл неможливо прочитати
     */
    private static List<String> readInput(Path input)
            throws IOException {

        return Files.readAllLines(
                input,
                StandardCharsets.UTF_8
        );
    }

    /**
     * Створює об'єкти UtilityReading та обчислює показники варіанта №9.
     *
     * @param lines рядки вхідного файла
     * @return сформований звіт або null, якщо коректних записів немає
     */
    private static String processLines(List<String> lines) {
        List<UtilityReading> readings = new ArrayList<>();

        for (int lineNumber = 1;
                lineNumber <= lines.size();
                lineNumber++) {

            String line = lines.get(lineNumber - 1);

            try {
                UtilityReading reading = UtilityReading.fromCsv(line);
                readings.add(reading);

                System.out.printf(
                        "Рядок %d: запис коректний%n",
                        lineNumber
                );
            } catch (IllegalArgumentException exception) {
                System.out.printf(
                        "Рядок %d пропущено: %s%n",
                        lineNumber,
                        exception.getMessage()
                );
            }
        }

        if (readings.isEmpty()) {
            System.out.printf(
                    "Не знайдено жодного коректного запису%n"
            );
            return null;
        }

        double totalConsumption = 0.0;
        double totalCost = 0.0;
        double maxConsumption = 0.0;

        for (UtilityReading reading : readings) {
            ConsumptionCost consumptionCost = new ConsumptionCost(
                    reading.getValue(),
                    reading.calculateCost()
            );

            totalConsumption += consumptionCost.consumption();
            totalCost += consumptionCost.cost();
            maxConsumption = Math.max(
                    maxConsumption,
                    consumptionCost.consumption()
            );
        }

        return formatReport(
                readings.size(),
                totalConsumption,
                totalCost,
                maxConsumption
        );
    }

    /**
     * Формує підсумковий текстовий звіт.
     *
     * @param validCount кількість коректних записів
     * @param totalConsumption сумарне споживання
     * @param totalCost загальна вартість
     * @param maxConsumption найбільше споживання
     * @return текст звіту
     */
    private static String formatReport(
            int validCount,
            double totalConsumption,
            double totalCost,
            double maxConsumption) {

        return String.format(
                Locale.ROOT,
                "Результати:%n"
                        + "Кількість коректних записів: %d%n"
                        + "Сумарне споживання: %.2f%n"
                        + "Загальна вартість: %.2f%n"
                        + "Найбільше споживання: %.2f%n",
                validCount,
                totalConsumption,
                totalCost,
                maxConsumption
        );
    }

    /**
     * Записує сформований звіт у файл у кодуванні UTF-8.
     *
     * @param output шлях до вихідного файла
     * @param report текст звіту
     * @throws IOException якщо звіт неможливо записати
     */
    private static void writeReport(
            Path output,
            String report) throws IOException {

        Path parent = output.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                output,
                report,
                StandardCharsets.UTF_8
        );
    }
}