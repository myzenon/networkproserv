import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.TimeUnit;

/**
 * TimeUnit.SECONDS.sleep(4);
 */
public class UDPServer implements Runnable {

    private DatagramSocket socket;
    private DatagramPacket receivePacket;
    private int waitTime;

    public UDPServer(DatagramSocket socket, DatagramPacket receivePacket, int waitTime) {
        this.socket = socket;
        this.receivePacket = receivePacket;
        this.waitTime = waitTime;
    }

    @Override
    public void run() {
        try {
            byte[] sendData = "success".getBytes();
            TimeUnit.SECONDS.sleep(waitTime);
            socket.send(new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort()));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}