import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
/**
 * Tianchen Liu
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private HashSet<String> topicSet = new HashSet<>();
    public final static int FILE_SIZE = 6022386;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
      
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
            handleLogoff();
           
        } catch (InterruptedException e) {
            e.printStackTrace();
            handleLogoff();
        } 
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ( (line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if ("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                }  else if ("list".equalsIgnoreCase(cmd)) {
                	sendUserList(outputStream, tokens);
                } else if ("sendFile".equalsIgnoreCase(cmd)) {
                	String sendTo = tokens[1];
                	String fileName = tokens[2];
                	
                	int bytesRead = 0;
                	System.out.println("Ready to accpet file");
                	byte [] mybytearray  = new byte [FILE_SIZE];
                	
                	bytesRead = inputStream.read(mybytearray,0,mybytearray.length);
                	System.out.println(bytesRead);
                    int current = bytesRead;
                    List<ServerWorker> workerList = server.getWorkerList();
                    String body = fileName;
                    for(ServerWorker worker : workerList) {
                    	if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    
                    	String outMsg = "fmsg " + login + " " + body + "\n";
                        worker.send(outMsg);
                       
                        worker.send(mybytearray);
                        
                        
                        
                    	}
                    	
                        // worker.send(mybytearray);
                     }
                    }
                	//sendUserList(outputStream, tokens);
               
                else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }
    
    private void handleFile (String[] tokens) {
    	
    	
    }

    private void handleLeave(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.remove(topic);
        }
    }

    public boolean isMemberOfTopic(String topic) {
        return topicSet.contains(topic);
    }

    private void handleJoin(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    // format: "msg" "login" body...
    // format: "msg" "#topic" body...
    private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';

        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker : workerList) {
            if (isTopic) {
                if (worker.isMemberOfTopic(sendTo)) {
                    String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            } else {
                if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            }
        }
    }

    private void handleLogoff() {
    	try {
        server.removeWorker(this);
        List<ServerWorker> workerList = server.getWorkerList();

        // send other online users current user's status
        String onlineMsg = "offline " + login + "\n";
        SimpleDateFormat formatter = new SimpleDateFormat(" MM-dd-yyyy HH:mm:ss");
//        String onlineMsg = login + " logged off on " + formatter.format(new Date()) + "\n";
        for(ServerWorker worker : workerList) {
            if (!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }
        }
        clientSocket.close();
    	}catch( IOException e){
    		 e.printStackTrace();
    		 
    	}
    }

    public String getLogin() {
        return login;
    }
    
    private void sendUserList(OutputStream outputStream, String[] tokens)  throws IOException{
    	 String login = tokens[1];
    	System.out.println("sending userlist " + login);
    	List<ServerWorker> workerList = server.getWorkerList();
    	 for(ServerWorker worker : workerList) {
             if (worker.getLogin() != null) {
                 if (!login.equals(worker.getLogin())) {
                     String msg2 = "online " + worker.getLogin() + "\n";
                     send(msg2);
                 }
             }
         }

         // send other online users current user's status
         String onlineMsg = "online " + login + "\n";
         for(ServerWorker worker : workerList) {
             if (!login.equals(worker.getLogin())) {
                 worker.send(onlineMsg);
             }
         }
    	
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat(" MM-dd-yyyy HH:mm:ss");
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

           // if ((login.equals("guest") && password.equals("guest")) || (login.equals("jim") && password.equals("jim")) ) {
            if ((login.equals(password)) ) {
                String msg = "ok login\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println(login + " has successfully logged in at" + formatter.format(new Date()));

                List<ServerWorker> workerList = server.getWorkerList();

                // send current user all other online logins
          /*      for(ServerWorker worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }

                // send other online users current user's status
                String onlineMsg = "online " + login + "\n";
                for(ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }*/
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    private void send(String msg) throws IOException {
        if (login != null) {
            try {
                outputStream.write(msg.getBytes());
                outputStream.flush();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void send(byte [] msg) throws IOException {
        if (login != null) {
            try {
                outputStream.write(msg);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
