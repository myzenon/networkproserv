package parameter;

public abstract class ParameterException extends Exception {
    private String consoleMessage;

    public String getConsoleMessage() {
        return consoleMessage;
    }

    public ParameterException(String message, String consoleMessage) {
        super(message);
        this.consoleMessage = consoleMessage;
    }
}
