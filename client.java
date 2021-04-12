import java.net.*;
import java.io.*;


public class client {
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("localhost", 4999);

       // PrintWriter pr = new PrintWriter(s.getOutputStream());
        //pr.println("hello");
       // pr.flush();
      
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        OutputStream out = socket.getOutputStream();

        String line;
        while((line = bf.readLine()) != null){
            System.out.println("Input Stream: "+ line); 
            out.write("Hi from client \n".getBytes());
        }



       // String str = bf.readLine();

       // System.out.println("Input Stream: "+ str); 
      
       //System.out.println("hello");
       socket.close();
    }
    
}
