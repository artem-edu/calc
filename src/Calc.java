import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;

public class Calc {
    private boolean isDirty = false;
    private double lastResult;

    private final Map<Character, Runnable> systemCommands = new HashMap<Character, Runnable>();
    private final Map<Character, DoubleBinaryOperator> operations = new HashMap<Character, DoubleBinaryOperator>();

    public Calc() {
        registerSystemCommands();
        registerOperations();
    }

    public void run() {
        Scanner input = new Scanner(System.in).useLocale(Locale.US);
        System.out.println("--- Консольный калькулятор ---");
        System.out.println("[c] - сброс, [s] - выход");

        Double a, b;
        Character operation;

        while (true) {
            if (isDirty) {
                a = lastResult;
            } else {
                a = inputResultResolver(getOperand(input, "Введите первое число: "));
                if (a == null) {
                    continue;
                }
                isDirty = true;
                lastResult = a;
            }

            operation = inputResultResolver(getMathOperation(input));

            if (operation == null) {
                continue;
            }

            b = inputResultResolver(getOperand(input, "Введите второе число: "));
            if (b == null) {
                continue;
            }

            calculate(a, b, operation);
        }
    }

    private void registerSystemCommands() {
        systemCommands.put('c', this::clear);
        systemCommands.put('s', this::shutdown);
    }

    private void registerOperations() {
        operations.put('+', this::add);
        operations.put('-', this::subtract);
        operations.put('*', this::multiply);
        operations.put('/', this::divide);
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

    private <T> T inputResultResolver(InputResult<T> inputResult) {
        if (inputResult.isSystemCommand()) {
            systemCommands.get(inputResult.getSystemCommand()).run();
            return null;
        }

        return inputResult.getValue();
    }

    private InputResult<Double> getOperand(Scanner input, String msg) {
        System.out.print(msg);
        while (true) {
            if (input.hasNextDouble()) {
                return InputResult.ofValue(input.nextDouble());
            }

            String value = input.next();
            char systemCommand = value.toLowerCase().charAt(0);

            if (tryHandeSystemCommand(systemCommand)) {
                return InputResult.ofSystemCommand(systemCommand);
            }

            System.out.println(value + " - не является числом");
            System.out.print(msg);
        }
    }

    private void calculate(double a, double b, char operation) {
        if (!checkMathOperation(operation)) {
            System.out.println("Неизвестная операция");
            return;
        }
        try {
            double result = operations.get(operation).applyAsDouble(a, b);
            lastResult = result;
            System.out.printf(Locale.US, "Результат: %.2f %c %.2f = %.2f%n", a, operation, b, result);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }

    }

    private boolean checkMathOperation(char operation) {
        return operations.containsKey(operation);
    }

    private boolean tryHandeSystemCommand(char command) {
        return systemCommands.containsKey(command);
    }

    private InputResult<Character> getMathOperation(Scanner input) {
        System.out.print("Введите операцию (+, -, *, /): ");
        while (true) {
            char operation = input.next().toLowerCase().charAt(0);
            if (checkMathOperation(operation)) {
                return InputResult.ofValue(operation);
            }

            if (tryHandeSystemCommand(operation)) {
                return InputResult.ofSystemCommand(operation);
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
            throw new ArithmeticException("Ошибка: делить на 0 нельзя.");
        }

        return a / b;
    }
}
