import java.io.Closeable;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class Server extends Thread {
    private HashMap<String, String> params;
    private ViewInterface view;
    private Closeable socket;
    private boolean loopCondition;
    private int waitTime = 2;

    public Server(HashMap<String, String> params, ViewInterface view) {
        this.params = params;
        this.view = view;
    }

    public void createTCPServer() {

        try {
            ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(this.params.get("p")));
            view.showMessage("Server TCP create at " + welcomeSocket.getInetAddress() + " port " + this.params.get("p"), "info");
            this.socket = welcomeSocket;
            this.loopCondition = true;
            while (loopCondition) {
                Socket socket = welcomeSocket.accept();
                new Thread(new TCPServer(socket, view, waitTime)).start();
            }
        }
        catch (BindException ex) {
            view.showMessage("This port " + this.params.get("p") + " is already in use.", "error");
        }
        catch (SocketException ex) {
            if(!ex.getMessage().equals("socket closed")) {
                ex.printStackTrace();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void createUDPServer() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(this.params.get("p")));
            view.showMessage("Server UDP create at port " + this.params.get("p"), "info");
            this.socket = serverSocket;
            this.loopCondition = true;
            while (loopCondition) {
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String data = new String(receivePacket.getData());
                    String newData = "";
                    for(char c : data.toCharArray()) {
                        if(c != 0) {
                            newData += c;
                        }
                    }

                    view.showMessage("The number is : " + newData + " from client " + receivePacket.getAddress() + " on port " + receivePacket.getPort(), "success");
                    new Thread(new UDPServer(serverSocket, receivePacket, waitTime)).start();
                }
                catch (SocketException ex) {
                    if(!ex.getMessage().equals("socket closed")) {
                        ex.printStackTrace();
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (BindException ex) {
            view.showMessage("This port " + this.params.get("p") + " is already in use.", "error");
        }
        catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if(params.get("t").equals("tcp")) {
                createTCPServer();
            }
            if(params.get("t").equals("udp")) {
                createUDPServer();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void interrupt() {
        try {
            this.loopCondition = false;
            this.socket.close();
            view.showMessage("Stop " + (this.params.get("t")).toUpperCase() + " Server at port " + this.params.get("p"), "info");
            view.showMessage("---------------------------------------------------", "info");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        super.interrupt();
    }
}
