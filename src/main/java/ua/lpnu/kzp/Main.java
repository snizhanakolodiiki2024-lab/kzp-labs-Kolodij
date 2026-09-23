package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас консольної програми лабораторної роботи №1.
 */
public final class Main {

    private static final String VERSION = "1.0.0";

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
        Path input = Path.of("data", "input.csv");
        Path output = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            if ("--help".equals(args[i])) {
                printHelp();
                return;
            }

            if ("--version".equals(args[i])) {
                printVersion();
                 return;
            }

            if ("--input".equals(args[i])) {
                if (i + 1 >= args.length) {
                    System.out.printf(
                            "Помилка: після --input потрібно вказати шлях до файла%n");
                    return;
                }

                input = Path.of(args[++i]);
                continue;
            }

            if ("--output".equals(args[i])) {
                if (i + 1 >= args.length) {
                    System.out.printf(
                            "Помилка: після --output потрібно вказати шлях до файла%n");
                    return;
                }

                output = Path.of(args[++i]);
                continue;
            }

            System.out.printf(
                    "Помилка: невідомий аргумент %s%n",
                    args[i]);
            return;
        }

        try {
            List<String> lines = readInput(input);

            System.out.printf(
                    "Прочитано рядків: %d%n",
                    lines.size());

            String report = processLines(lines);

            if (report == null) {
                return;
            }

            System.out.printf("%n%s", report);

            writeReport(output, report);

            System.out.printf(
                    "Звіт записано у файл: %s%n",
                    output);

        } catch (IOException exception) {
            System.out.printf(
                    "Помилка роботи з файлом: %s%n",
                    exception.getMessage());
        }
    }

    /**
     * Виводить довідку про параметри командного рядка.
     */
    private static void printHelp() {
    System.out.printf(
            "Використання: java -jar <файл.jar> "
                    + "[--help] [--version] "
                    + "[--input <файл>] [--output <файл>]%n");
    }

    /**
    * Виводить версію програми.
    */
    private static void printVersion() {
    String buildNumber = System.getenv("CI_BUILD_NUMBER");

    if (buildNumber == null || buildNumber.isBlank()) {
        System.out.printf(
                "Version %s%n",
                VERSION);
        return;
    }

    System.out.printf(
            "Version %s (build %s)%n",
            VERSION,
            buildNumber);
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
                StandardCharsets.UTF_8);
    }

    /**
     * Перевіряє записи та обчислює показники варіанта №9.
     *
     * @param lines рядки вхідного файла
     * @return сформований звіт або null, якщо коректних записів немає
     */
    private static String processLines(List<String> lines) {
        int validCount = 0;
        double totalConsumption = 0.0;
        double totalCost = 0.0;
        double maxConsumption = 0.0;

        for (int lineNumber = 1;
                lineNumber <= lines.size();
                lineNumber++) {

            String line = lines.get(lineNumber - 1);

            if (line.isBlank()) {
                System.out.printf(
                        "Рядок %d пропущено: порожній рядок%n",
                        lineNumber);
                continue;
            }

            String[] fields = line.split(";", -1);

            if (fields.length != 4) {
                System.out.printf(
                        "Рядок %d пропущено: очікується 4 поля%n",
                        lineNumber);
                continue;
            }

            String meter = fields[0].trim();
            String date = fields[1].trim();

            if (meter.isEmpty()) {
                System.out.printf(
                        "Рядок %d пропущено: "
                                + "поле meter не може бути порожнім%n",
                        lineNumber);
                continue;
            }

            if (date.isEmpty()) {
                System.out.printf(
                        "Рядок %d пропущено: "
                                + "поле date не може бути порожнім%n",
                        lineNumber);
                continue;
            }

            double value;
            double tariff;

            try {
                value = Double.parseDouble(fields[2].trim());
                tariff = Double.parseDouble(fields[3].trim());
            } catch (NumberFormatException exception) {
                System.out.printf(
                        "Рядок %d пропущено: "
                                + "value і tariff мають бути числами%n",
                        lineNumber);
                continue;
            }

            if (value < 0) {
                System.out.printf(
                        "Рядок %d пропущено: "
                                + "value не може бути від'ємним%n",
                        lineNumber);
                continue;
            }

            if (tariff < 0) {
                System.out.printf(
                        "Рядок %d пропущено: "
                                + "tariff не може бути від'ємним%n",
                        lineNumber);
                continue;
            }

            validCount++;
            totalConsumption += value;
            totalCost += value * tariff;

            if (value > maxConsumption) {
                maxConsumption = value;
            }

            System.out.printf(
                    "Рядок %d: запис коректний%n",
                    lineNumber);
        }

        if (validCount == 0) {
            System.out.printf(
                    "Не знайдено жодного коректного запису%n");
            return null;
        }

        return formatReport(
                validCount,
                totalConsumption,
                totalCost,
                maxConsumption);
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
                maxConsumption);
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
                StandardCharsets.UTF_8);
    }
}