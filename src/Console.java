import parameter.Parameter;
import parameter.ParameterException;

import java.util.HashMap;

public class Console implements ViewInterface {

    private HashMap<String, String> params;

    public Console(HashMap<String, String> params) {
        this.params = params;
    }

    @Override
    public void showMessage(String message, String messageType) {
        if(messageType.equals("info")) {
            System.out.println("Info : " + message);
        }
        if(messageType.equals("error")) {
            System.err.println("Error : " + message);
        }
        if(messageType.equals("success")) {
            System.out.println("Success : " + message + " **");
        }
    }

    @Override
    public void create() {
        try {
            Parameter.checkNullParameter(this.params);
            Parameter.checkParameter(this.params);
            new Thread(new Server(params, this)).start();
        }
        catch (ParameterException ex) {
            showMenuWithError(ex);
        }
    }

    public static void showMenu() {
        System.out.println();
        System.out.println(" [ This program require all of following argument in the same format. ]");
        System.out.println("   By Zenon 'SI | si.zenon@gmail.com");
        System.out.println();
        System.out.println("\t-t <UDP|TCP> : UDP and TCP are type of protocol to connect server");
        System.out.println("\t-p <Port Number> : the port being used by the server [1 - 65535]");
        System.out.println("\n\t ** -gui : To start program on graphic mode [optional]");
        System.out.println();
    }

    public static void showMenuWithError(ParameterException ex) {
        System.out.println();
        if(ex.getConsoleMessage() != null) {
            System.err.println(" *** " + ex.getConsoleMessage());
        }
        System.err.println(" **** " + ex.getMessage());
        showMenu();
    }



}
