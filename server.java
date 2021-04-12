import java.net.*;





import java.io.*;



class server{


    
    



public void listen(InetAddress addr){
    try{
        ServerSocket ss = new ServerSocket(4999, 50, addr);
        Socket s = ss.accept();
    }catch(IOException e){
        System.out.println(e);
    }
}
public static void main(String[] args) {
    try{
    String ip = "127.0.0.1";
    InetAddress addr = InetAddress.getByName(ip);
    ServerSocket serverSocket = new ServerSocket(4999);
    while(true) {
        System.out.println("Accepting client connection...");
        Socket clientSocket = serverSocket.accept();
        Thread t = new Thread() {
            @Override
            public void run(){
                try{
                    handleClientSocket(clientSocket);
                } catch (IOException e){
                    e.printStackTrace();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };

        t.start();

    }
    

    //InputStreamReader in = new InputStreamReader((clientSocket.getInputStream()));

    //BufferedReader bf = new BufferedReader(in);

    //String str = bf.readLine();
    //System.out.println("client: " + str);
}catch (IOException e){
    e.printStackTrace();
}





}


private static void handleClientSocket(Socket clientSocket) throws IOException, InterruptedException{
    OutputStream out = clientSocket.getOutputStream();
    InputStreamReader in = new InputStreamReader(clientSocket.getInputStream());
    BufferedReader bf = new BufferedReader(in);
    String line;
    for(int i=0; i < 10; i++) {
        out.write("Time now is \n".getBytes());
        line = bf.readLine();
        System.out.println("Input Stream: "+ line); 
        Thread.sleep(1000);
    }
    clientSocket.close();
}
}