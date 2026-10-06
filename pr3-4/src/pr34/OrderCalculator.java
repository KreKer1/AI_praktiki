package pr34;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class OrderCalculator {
    private static final double VAT_RATE = 20.0;
    // Вариант 3: порог бесплатной обработки = 20
    private static final int FREE_PROCESSING_THRESHOLD = 20;
    private static final double PROCESSING_FEE = 500.0;

    public static double calculateBase(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }

    public static double applyDiscount(double base, double discountPercent) {
        return base * (1 - discountPercent / 100);
    }

    public static double calculateVat(double discounted, double vatPercent) {
        return discounted * vatPercent / 100;
    }

    public static double calculateTotal(int quantity, double unitPrice, double discountPercent, double vatPercent) {
        double base = calculateBase(quantity, unitPrice);
        double discounted = applyDiscount(base, discountPercent);
        double vat = calculateVat(discounted, vatPercent);
        return roundToKopecks(discounted + vat);
    }

    public static double calculateProcessingFee(int quantity) {
        if (quantity >= FREE_PROCESSING_THRESHOLD) {
            return 0.0;
        } else {
            return PROCESSING_FEE;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US);
        try {
            System.out.print("Количество: ");
            int quantity = in.nextInt();
            System.out.print("Цена единицы руб.: ");
            double unitPrice = in.nextDouble();
            System.out.print("Скидка, %: ");
            double discountPercent = in.nextDouble();

            if (isValid(quantity, unitPrice, discountPercent)) {
                double total = calculateTotal(quantity, unitPrice, discountPercent, VAT_RATE)
                        + calculateProcessingFee(quantity);
                System.out.printf("Итого: %.2f руб.%n", total);
            } else {
                System.out.println("Ошибка: значения вне допустимых диапазонов");
            }
        } catch (InputMismatchException e) {
            System.out.println("Ошибка: нужно вводить числа (дробные через точку)");
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка: значение не введено");
        }
    }

    static double roundToKopecks(double value) {
        return Math.round(value * 100) / 100.0;
    }

    public static boolean isValid(int quantity, double unitPrice, double discountPercent) {
        return quantity >= 1 && quantity <= 10000
                && unitPrice > 0 && unitPrice <= 5000000
                && discountPercent >= 0 && discountPercent <= 30;
    }
}
