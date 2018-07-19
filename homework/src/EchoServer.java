import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    private int port;
    public boolean isRunning;
    private static int DEFAULT_PORT = 8080;
    private Socket client = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public EchoServer(int port) {
        this.port = port;
        this.isRunning = false;
    }

    public EchoServer() {
        this(DEFAULT_PORT);
    }

    public int getPort() {
        return  port;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(port)){
            client = server.accept();
            System.out.println("Connection accepted");
            out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream created");
            in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");
            isRunning = true;
            while (!client.isClosed()) {
                System.out.println("Server reading");
                String entry = in.readUTF();
                System.out.println("client message: " + entry);
                System.out.println("Server try writing to channel");
                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client inizialized closed the chanel");
                    out.writeUTF("Server reply " + entry + " :OK");
                    out.flush();
                    Thread.sleep(3000);
                    break;
                }
                out.writeUTF("Server reply: " + entry);
                out.flush();
            }
            stop();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stop()  {
        System.out.println("Client disconnected, closing channel & ");
        try {
            in.close();
            out.close();
            client.close();
            isRunning = false;
            System.out.println("Connection closed - DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
