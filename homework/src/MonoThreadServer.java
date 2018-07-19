import javax.imageio.IIOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadServer implements Runnable {
    private static Socket clientDialog;

    public MonoThreadServer(Socket client) {
        MonoThreadServer.clientDialog = client;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            this.log("DataInputStream created");
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            this.log("DataOutputStream created");
            while (!clientDialog.isClosed()) {
                this.log("MonoThreadServer reading from channel");
                String reqest = in.readUTF();
                this.log("client message: " + reqest);
                if (reqest.equalsIgnoreCase("quit")) {
                    out.writeUTF(reqest + "Ok");
                    Thread.sleep(3000);
                    break;
                }
                out.writeUTF("MonoThreadServer response: " + reqest);
                out.flush();
            }
            this.log(" MonoThreadServer connection close");
            in.close();
            out.close();
            clientDialog.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void log(String message) {
        System.out.println(message);
    }

}
