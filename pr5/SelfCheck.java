package pr5;

import java.util.Arrays;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = TimesheetAnalyzer.total(new int[]{8, 8, 8, 8, 8}) == 40
                && TimesheetAnalyzer.overtimeDays(new int[]{9, 8, 7, 10, 8}) == 2;
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
        int failed = 0;
        failed += check("9;8;7;10;8 разбирается", Arrays.equals(TimesheetAnalyzer.parseHours("9;8;7;10;8"), new int[]{9, 8, 7, 10, 8}));
        failed += check("сумма 9;8;7;10;8 = 42", TimesheetAnalyzer.total(new int[]{9, 8, 7, 10, 8}) == 42);
        failed += check("среднее 9;8;7;10;8 = 8.4", Math.abs(TimesheetAnalyzer.average(new int[]{9, 8, 7, 10, 8}) - 8.4) < 1e-9);
        failed += check("дней с переработкой: ровно 8 не считается", TimesheetAnalyzer.overtimeDays(new int[]{8, 8, 8, 8, 8}) == 0);
        failed += check("недельная переработка 8;8;8;8;8 = 20 (сумма 40 − норма 20)", TimesheetAnalyzer.weeklyOvertime(new int[]{8, 8, 8, 8, 8}) == 20);
        failed += check("сумма ровно 20 -> переработка 0", TimesheetAnalyzer.weeklyOvertime(new int[]{4, 4, 4, 4, 4}) == 0);
        failed += check("сумма 21 -> переработка 1", TimesheetAnalyzer.weeklyOvertime(new int[]{5, 4, 4, 4, 4}) == 1);
        failed += check("сумма меньше нормы -> переработка не отрицательная", TimesheetAnalyzer.weeklyOvertime(new int[]{0, 0, 0, 0, 0}) == 0);
        failed += check("значение 0 допустимо", parses("0;0;0;0;0"));
        failed += check("значение 16 допустимо", parses("16;16;16;16;16"));
        failed += check("значение 17 -> ошибка с позицией 3", errorContains("8;8;17;8;8", "позиция 3"));
        failed += check("значение -1 -> ошибка с позицией 1", errorContains("-1;8;8;8;8", "позиция 1"));
        failed += check("пустая строка -> ошибка", errorContains("", "пустая строка"));
        failed += check("пустое значение на позиции 2 -> ошибка", errorContains("8;;8;8;8", "позиция 2"));
        failed += check("4 значения вместо 5 -> ошибка", errorContains("8;8;8;8", "получено 4"));
        failed += check("'abc' на позиции 2 -> ошибка", errorContains("8;abc;8;8;8", "позиция 2"));
        failed += check("дробное 8.5 -> ошибка", errorContains("8;8;8.5;8;8", "позиция 3"));
        failed += check("6 значений вместо 5 -> ошибка", errorContains("8;8;8;8;8;8", "получено 6"));
        failed += check("analyze: корректная строка без ошибки", !TimesheetAnalyzer.analyze("8;8;8;8;8").startsWith("ОШИБКА"));
        failed += check("analyze: ошибка одного значения -> весь табель ОШИБКА", TimesheetAnalyzer.analyze("8;8;17;8;8").startsWith("ОШИБКА"));
        System.out.println(failed == 0 ? "ВСЕ ПРОВЕРКИ ПРОЙДЕНЫ" : "ПРОВАЛЕНО: " + failed);
    }
    private static boolean parses(String line) {
        try {
            TimesheetAnalyzer.parseHours(line);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    private static boolean errorContains(String line, String fragment) {
        try {
            TimesheetAnalyzer.parseHours(line);
            return false;
        } catch (IllegalArgumentException e) {
            return e.getMessage().contains(fragment);
        }
    }
    private static int check(String name, boolean condition) {
        System.out.println((condition ? "  ok   " : "  FAIL ") + name);
        return condition ? 0 : 1;
    }
}