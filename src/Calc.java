import java.util.Locale;
import java.util.Scanner;

public class Calc {
    private boolean isDirty = false;
    private double lastResult;

    public void run() {
        Scanner input = new Scanner(System.in).useLocale(Locale.US);
        System.out.println("--- Консольный калькулятор ---");
        System.out.println("[c] - сброс, [s] - выход");

        double a, b;
        char operation;

        /**
         * Осознанно нет выхода из цикла,
         * так как выход вызывается методом shutdown()
         * метод может быть вызван на всех этапах ввода символом "S"
         */
        while (true) {
            if (isDirty) {
                a = lastResult;
            } else {
                a = getOperand(input, "Введите первое число: ");

                if (Double.isNaN(a)) {
                    continue;
                }

                isDirty = true;
            }

            operation = getMathOperation(input);

            if (operation == '\0') {
                continue;
            }

            b = getOperand(input, "Введите второе число: ");
            if (Double.isNaN(b)) {
                continue;
            }

            calculate(a, b, operation);
        }
    }

    private void clear() {
        System.out.println("Сброс результата");
        isDirty = false;
        lastResult = 0;
    }

    private void shutdown() {
        System.out.println("Выход из программы.");
        System.exit(0);
    }

    /**
     *
     * @param input
     * @param msg
     * @return возвращаем число или NaN - в случае если введенная команды найдена в системных командах
     */
    private double getOperand(Scanner input, String msg) {
        System.out.print(msg);
        while (true) {
            if (input.hasNextDouble()) {
                return input.nextDouble();
            }

            String value = input.next();
            char operation = value.toLowerCase().charAt(0);

            if (tryHandeSystemCommand(operation)) {
                return Double.NaN;
            }

            System.out.println(value + " - не является числом");
            System.out.print(msg);
        }
    }

    private void calculate(double a, double b, char operation) {
        double result;
        switch (operation) {
            case '+':
                result = add(a, b);
                break;
            case '-':
                result = subtract(a, b);
                break;
            case '*':
                result = multiply(a, b);
                break;
            case '/':
                result = divide(a, b);
                break;
            default:
                System.out.println("Неизвестная операция");
                return;
        }

        if (Double.isNaN(result)) {
            System.out.println("Ошибка: делить на 0 нельзя.");
            return;
        }
        lastResult = result;
        System.out.printf(Locale.US, "Результат: %.2f %c %.2f = %.2f%n", a, operation, b, result);
    }

    private boolean checkMathOperation(char operation) {
        switch (operation) {
            case '+':
            case '-':
            case '/':
            case '*':
                return true;
            default:
                return false;
        }
    }

    private boolean tryHandeSystemCommand(char command) {
        switch (command) {
            case 'c':
                clear();
                return true;
            case 's':
                shutdown();
                return true;
            default:
                return false;
        }
    }

    /**
     *
     * @param input
     * @return возвращает символ команды или \0 -
     * в случае если была введена системная команда
     */
    private char getMathOperation(Scanner input) {
        System.out.print("Введите операцию (+, -, *, /): ");
        while (true) {
            char operation = input.next().toLowerCase().charAt(0);
            if (checkMathOperation(operation)) {
                return operation;
            }

            if (tryHandeSystemCommand(operation)) {
                return '\0';
            }

            System.out.println("Неизвестная операция");
            System.out.print("Введите операцию (+, -, *, /): ");
        }
    }

    private double add(double a, double b) {
        return a + b;
    }
    private double subtract(double a, double b) {
        return a - b;
    }
    private double multiply(double a, double b) {
        return a * b;
    }
    private double divide(double a, double b) {
        if (b == 0) {
            // тут стоит выбросить исключение, но так как, мы их не проходили,
            // более честно будет вернуть NaN
            return Double.NaN;
        }

        return a / b;
    }
}
