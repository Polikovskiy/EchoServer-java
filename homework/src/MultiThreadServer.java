import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadServer implements Runnable{
    public boolean isRunning;
    
    private int port;
    private static int DEFAULT_PORT = 8080;
    private ServerSocket server = null;
    private static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    public MultiThreadServer(int port) {
        this.port = port;
        this.isRunning = false;
    }

    public MultiThreadServer() {
        this(DEFAULT_PORT);
    }

    public int getPort() {
        return  port;
    }

    public boolean isRunning() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isRunning;
    }


    @Override
    public void  run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            server = new ServerSocket(port);
            this.log("Main server created");
            isRunning = true;

            Thread th =new Thread(){
                @Override
                public void run(){
                    try {
                        while (!server.isClosed()) {
                            Socket client = server.accept();
                            executeIt.execute(new MonoThreadServer(client));
                            this.log("Main server accept");
                        }
                    }catch (SocketException e)
                    {
                        this.log("Socket was closed by main process");
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            };
            th.start();
            while (true) {
                if (br.ready()) {
                    this.log("Main server found any messages");
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        this.log("Main server is closed");
                        stop();
                        break;
                    }
                }

            }
            this.log("Main server stopped");
            executeIt.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.close();
            isRunning = false;
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public void start() {
        executeIt.execute(this);
    }
    
    public void log(String message) {
        System.out.println(message);
    }    
}
