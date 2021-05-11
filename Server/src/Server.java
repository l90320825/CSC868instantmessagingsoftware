import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Tianchen Liu
 */
public class Server extends Thread {


    private final int serverPort;
    public ServerPanel serverPanel;

    public Server(int serverPort, ServerPanel serverPanel){
        this.serverPort = serverPort;
        this.serverPanel = serverPanel;
    }
    private ArrayList<ServerWorker> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkerList() {
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            SimpleDateFormat formatter = new SimpleDateFormat(" MM-dd-yyyy HH:mm:ss");
            while(true) {
                System.out.println("Accepting client connection...");
                serverPanel.adding("Accepting client connection...\n");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket + formatter.format(new Date()));
                serverPanel.adding("Accepted connection from " + clientSocket + " \n at " + formatter.format(new Date()) + "\n");
                ServerWorker worker = new ServerWorker(this, clientSocket);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }
}
