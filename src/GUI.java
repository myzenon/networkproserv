import parameter.Parameter;
import parameter.ParameterException;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

public class GUI implements ViewInterface {
    private JPanel panel;
    private JTextPane status;
    private JButton startServerButton;
    private JTextField port;
    private JRadioButton TCPRadioButton;
    private JRadioButton UDPRadioButton;

    private HashMap<String, String> params;
    private GUI viewGUI;
    private Thread serverThread;
    private String statusText = "";

    public GUI(HashMap<String, String> params) {
        this.params = params;
        this.viewGUI = this;
        this.status.setContentType("text/html");
        setParametersEachField();
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(serverThread == null) {
                    assignParameter();
                    startServer();
                }
                else {
                    stopServer();
                }
            }
        });
        status.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                status.setEditable(true);

            }
            @Override
            public void focusGained(FocusEvent e) {
                status.setEditable(false);
            }
        });
    }

    public void startServer() {
        try {
            Parameter.checkNullParameter(params);
            Parameter.checkParameter(params);
            disableField();
            serverThread = new Server(params, viewGUI);
            serverThread.start();
            startServerButton.setText("Stop Server");
        }
        catch (ParameterException ex) {
            showMessage(ex.getMessage(), "error");
            enableField();
        }
    }

    public void stopServer() {
        serverThread.interrupt();
        serverThread = null;
        enableField();
    }




    public void enableField() {
        port.setEnabled(true);
        TCPRadioButton.setEnabled(true);
        UDPRadioButton.setEnabled(true);
        startServerButton.setText("Start Server");
    }

    public void disableField() {
        port.setEnabled(false);
        TCPRadioButton.setEnabled(false);
        UDPRadioButton.setEnabled(false);
        startServerButton.setText("Stop Server");
    }

    public void setParametersEachField() {
        if(params.get("t") != null) {
            if(params.get("t").equals("tcp")) {
                TCPRadioButton.setSelected(true);
            }
            else if(params.get("t").equals("udp")) {
                UDPRadioButton.setSelected(true);
            }
        }
        if(params.get("p") != null) {
            port.setText(params.get("p"));
        }
    }

    public void assignParameter() {
        if(TCPRadioButton.isSelected()) {
            params.put("t", "tcp");
        }

        if(UDPRadioButton.isSelected()) {
            params.put("t", "udp");
        }

        if(port.getText().equals("")) {
            params.put("p", null);
        }
        else {
            params.put("p", port.getText());
        }
    }


    @Override
    public void showMessage(String message, String messageType) {
        if(messageType.equals("info")) {
            statusText = "<span style=\"color : blue\"><strong>Info : </strong>" + message + "</span><br>" + statusText;
        }
        if(messageType.equals("error")) {
            statusText = "<span style=\"color : red\"><strong>Error : </strong>" + message + "</span><br>" + statusText;
            enableField();
        }
        if(messageType.equals("success")) {
            statusText = "<span style=\"color : green\"><strong>Success : </strong>" + message + "</span><br>" + statusText;
        }
        status.setText(statusText);
    }

    @Override
    public void create() {
        JFrame frame = new JFrame("Server Program");
        frame.setContentPane(this.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
