import parameter.Parameter;
import parameter.ParameterMisFormatException;

import java.util.HashMap;


public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            Console.showMenu();
        }
        else {
            try {
                HashMap<String, String> params = Parameter.mapParameter(args);
                if(params.get("gui").equals("on")) {
                    new GUI(params).create();
                }
                else {
                    new Console(params).create();
                }
            }
            catch (ParameterMisFormatException ex) {
                Console.showMenuWithError(ex);
            }
        }

    }
}
