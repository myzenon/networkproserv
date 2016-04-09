import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;


public class TCPServer implements Runnable {

    private Socket socket;
    private ViewInterface view;
    private int waitTime;

    public TCPServer(Socket socket, ViewInterface view, int waitTime) {
        this.socket = socket;
        this.view = view;
        this.waitTime = waitTime;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientMessage = inFromClient.readLine();
            if(clientMessage != null) {
                view.showMessage("The number is : " + clientMessage + " from client " + socket.getInetAddress() + " on port " + socket.getPort(), "success");
            }
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            TimeUnit.SECONDS.sleep(waitTime);
            outToClient.writeBytes("success\n");
            socket.close();
        }
        catch (SocketException ex) {
            if(!ex.getMessage().equals("Software caused connection abort: socket write error")) {
                ex.printStackTrace();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}