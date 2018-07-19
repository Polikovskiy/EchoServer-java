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
            System.out.println("DataInputStream created");
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            System.out.println("DataOutputStream created");
            while (!clientDialog.isClosed()) {
                System.out.println("MonoThreadServer reading from channel");
                String reqest = in.readUTF();
                System.out.println("client message: " + reqest);
                if (reqest.equalsIgnoreCase("quit")) {
                    out.writeUTF(reqest + "Ok");
                    Thread.sleep(3000);
                    break;
                }
                out.writeUTF("MonoThreadServer response: " + reqest);
                out.flush();
            }
            System.out.println(" MonoThreadServer connection close");
            in.close();
            out.close();
            clientDialog.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
