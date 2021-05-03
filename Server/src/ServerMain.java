
/**
 * Tianchen Liu
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = 8818; //modify port number here!
        Server server = new Server(port);
        server.start();
    }
}
