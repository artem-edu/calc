public class InputResult<T> {
    private final T value;
    private final Character systemCommand;

    private InputResult(T value, Character systemCommand) {
        this.value = value;
        this.systemCommand = systemCommand;
    }

    public static <T> InputResult<T> ofValue(T value) {
        return new InputResult<>(value, null);
    }

    public static <T> InputResult<T> ofSystemCommand(char systemCommand) {
        return new InputResult<>(null, systemCommand);
    }

    public boolean isSystemCommand() {
        return systemCommand != null;
    }

    public T getValue() {
        return value;
    }

    public char getSystemCommand() {
        return systemCommand;
    }
}