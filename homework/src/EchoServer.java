import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    private static final int REPLAY_DELAY = 3000;
    private int port;
    private static int DEFAULT_PORT = 8080;
    private Socket client = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    
    public boolean isRunning;

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
            this.acceptConnection(server);
            while (!client.isClosed()) {
                String incommingMessage = this.readIncommingMessage();
                this.writeReplay(incommingMessage);
            }
            stop();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    private void acceptConnection(ServerSocket server) {
        client = server.accept();
        this.log("Connection accepted");
        out = new DataOutputStream(client.getOutputStream());
        this.log("DataOutputStream created");
        in = new DataInputStream(client.getInputStream());
        this.log("DataInputStream created");
        isRunning = true;    
    }
    
    private String readIncommingMessage() {
        this.log("Server reading");
        String entry = in.readUTF();
        this.log("client message: " + entry);
        //this.log("Server try writing to channel"); 
        return entry;
    }
    
    private void writeReplay(String incommingMessage) {
        if (incommingMessage.equalsIgnoreCase("quit")) {
                    this.log("Client inizialized closed the chanel");
                    out.writeUTF("Server reply " + incommingMessage + " :OK");
                    out.flush();
                    Thread.sleep(REPLAY_DELAY);
                    break;
         }
         out.writeUTF("Server reply: " + incommingMessage);
         out.flush();
    }    
    
    public void stop()  {
        this.log("Client disconnected, closing channel & ");
        try {
            in.close();
            out.close();
            client.close();
            isRunning = false;
            this.log("Connection closed - DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void log(String message) {
        System.out.println(message);
    }
    
}
