package parameter;

/**
 * Created by Zenon on 3/27/2016 AD.
 */
public class ParameterMisMatchException extends ParameterException {
    public ParameterMisMatchException(String message, String consoleMessage) {
        super(message, consoleMessage);
    }
}
