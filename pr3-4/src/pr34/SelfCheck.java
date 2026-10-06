package pr34;

public class SelfCheck {
    public static void main(String[] args) {
        int failed = 0;

        // основной расчёт
        failed += check("2 x 100, скидка 10%, НДС 20% -> 216",
                OrderCalculator.calculateTotal(2, 100.0, 10.0, 20.0) == 216.0);
        failed += check("округление: 3 x 0.1 -> 0.36",
                OrderCalculator.calculateTotal(3, 0.1, 0, 20) == 0.36);

        // количество: границы 1 и 10000
        failed += check("количество 1 допустимо", OrderCalculator.isValid(1, 100, 0));
        failed += check("количество 0 недопустимо", !OrderCalculator.isValid(0, 100, 0));
        failed += check("количество 10000 допустимо", OrderCalculator.isValid(10000, 100, 0));
        failed += check("количество 10001 недопустимо", !OrderCalculator.isValid(10001, 100, 0));

        // цена: граница 0 и 5 000 000
        failed += check("цена 0 недопустима", !OrderCalculator.isValid(1, 0, 0));
        failed += check("цена 5000000 допустима", OrderCalculator.isValid(1, 5000000, 0));
        failed += check("цена 5000000.01 недопустима", !OrderCalculator.isValid(1, 5000000.01, 0));

        // скидка: границы 0 и 30
        failed += check("скидка 0 допустима", OrderCalculator.isValid(1, 100, 0));
        failed += check("скидка -0.01 недопустима", !OrderCalculator.isValid(1, 100, -0.01));
        failed += check("скидка 30 допустима", OrderCalculator.isValid(1, 100, 30));
        failed += check("скидка 30.01 недопустима", !OrderCalculator.isValid(1, 100, 30.01));

        // вариант 3: порог 20
        failed += check("19 единиц -> плата 500", OrderCalculator.calculateProcessingFee(19) == 500.0);
        failed += check("20 единиц -> бесплатно", OrderCalculator.calculateProcessingFee(20) == 0.0);

        System.out.println(failed == 0 ? "ВСЕ ПРОВЕРКИ ПРОЙДЕНЫ" : "ПРОВАЛЕНО: " + failed);
    }

    private static int check(String name, boolean condition) {
        System.out.println((condition ? "  ok   " : "  FAIL ") + name);
        return condition ? 0 : 1;
    }
}
