package pr5;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class TimesheetAnalyzer {
    static final int DAYS = 5;
    static final int MIN_HOURS = 0;
    static final int MAX_HOURS = 16;
    static final int DAILY_NORM = 8;
    static final int WEEKLY_NORM = 20;
    public static int[] parseHours(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("пустая строка, ожидалось " + DAYS + " значений через ';'");
        }
        String[] parts = line.split(";", -1);
        if (parts.length != DAYS) {
            throw new IllegalArgumentException("ожидалось " + DAYS + " значений, получено " + parts.length);
        }
        int[] hours = new int[DAYS];
        for (int i = 0; i < parts.length; i++) {
            int position = i + 1;
            String text = parts[i].trim();
            if (text.isEmpty()) {
                throw new IllegalArgumentException("позиция " + position + ": пустое значение");
            }
            int value;
            try {
                value = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("позиция " + position + ": «" + text + "» не является целым числом");
            }
            if (value < MIN_HOURS || value > MAX_HOURS) {
                throw new IllegalArgumentException("позиция " + position + ": значение " + value
                        + " вне диапазона " + MIN_HOURS + ".." + MAX_HOURS);
            }
            hours[i] = value;
        }
        return hours;
    }

    public static int total(int[] hours) {
        int sum = 0;
        for (int i = 0; i < hours.length; i++) {
            sum += hours[i];
        }
        return sum;
    }

    public static double average(int[] hours) {
        if (hours.length == 0) {
            return 0.0;
        }
        return (double) total(hours) / hours.length;
    }
    public static int overtimeDays(int[] hours) {
        int count = 0;
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] > DAILY_NORM) {
                count++;
            }
        }
        return count;
    }
    public static int weeklyOvertime(int[] hours) {
        return Math.max(0, total(hours) - WEEKLY_NORM);
    }
    public static String analyze(String line) {
        int[] hours;
        try {
            hours = parseHours(line);
        } catch (IllegalArgumentException e) {
            return "ОШИБКА: " + e.getMessage();
        }
        return String.format("сумма %d ч; среднее %.2f ч/день; дней с переработкой %d; недельная переработка %d ч",
                total(hours), average(hours), overtimeDays(hours), weeklyOvertime(hours));
    }
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            List<String> lines = Files.readAllLines(Path.of(args[0]), StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                System.out.println("Строка " + (i + 1) + " [" + lines.get(i) + "]: " + analyze(lines.get(i)));
            }
        } else {
            Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
            System.out.print("Часы за 5 дней через ';' (например 8;8;8;8;8): ");
            String line = in.hasNextLine() ? in.nextLine() : "";
            System.out.println(analyze(line));
        }
    }
}