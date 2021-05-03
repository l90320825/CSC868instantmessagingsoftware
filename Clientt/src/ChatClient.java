
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;



public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;
    public final static String
    FILE_TO_RECEIVED = "c:/Users/johnt/Desktop/New folder/a7.jpg";
    public final static int FILE_SIZE = 6022386;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();
    private ArrayList<ClientHandler> messageListener = new ArrayList<>();
    

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
    	
    	
        ChatClient client = new ChatClient("localhost", 8818);
     
        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
            }
        });
       
        
        /*
        client.addMessageListener(new ClientHandler() {
        	 @Override
             public void online(String login) {
                 System.out.println("ONLINE: " + login);
             }

             @Override
             public void offline(String login) {
                 System.out.println("OFFLINE: " + login);
             }
             
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("You got a message from " + fromLogin + " ===>" + msgBody);
            }
        });*/
        
      

        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("You got a message from " + fromLogin + " ===>" + msgBody);
            }
        });

        if (!client.connect()) {
            System.err.println("Connect failed.");
        } else {
            System.out.println("Connect successful");

            

            //client.logoff();
        }
    }
    
    public void getList(String login) {//get online user from server
    	try {
        String cmd = "list " + login + "\n";
        serverOut.write(cmd.getBytes());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public void msg(String sendTo, String msgBody) throws IOException {//a object send message
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        serverOut.write(cmd.getBytes());
    }
    
    public void sendFile(byte []  fileData, String sendTo, String fileName)  throws IOException {//method call server to ready accept file
    	String cmd = "sendFile " + sendTo + " " + fileName + " \n";
    	serverOut.write(cmd.getBytes());
    	serverOut.flush();
    	System.out.println("Chatclient sending " + fileData.length);
    	serverOut.write(fileData,0,fileData.length);
    	serverOut.flush();
    	
    	
    }

    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response Line:" + response);

        if ("ok login".equalsIgnoreCase(response)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    public void logoff() throws IOException {
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    private void startMessageReader() {// a thread to read message from server
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    private void readMessageLoop() {
        try {
            String line;
            while ((line = bufferedIn.readLine()) != null) {
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        handleOffline(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        System.out.println(tokensMsg[1]);
                        handleMessage(tokensMsg);
                    } else if ("fmsg".equalsIgnoreCase(cmd)) {//on file
                    	System.out.println("fmsg recevied");
                    	System.out.println(line);
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        
                        System.out.println(tokensMsg[1]);
                        handleMessage(tokensMsg);
                        
                        
                        
                        byte [] mybytearray  = new byte [FILE_SIZE];
                        FileOutputStream fos = new FileOutputStream(FILE_TO_RECEIVED);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        int bytesRead = serverIn.read(mybytearray,0,mybytearray.length);
                        System.out.println("recevied " + bytesRead);
                        bos.write(mybytearray, 0 , bytesRead);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for(MessageListener listener : messageListeners) {
            listener.onMessage(login, msgBody);
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.online(login);
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Client port is " + socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUserStatusListener(UserStatusListener listener) {
        userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }
    
    /*
    
    public void addMessageListener(ClientHandler listener) {
        messageListener.add(listener);
    }

    public void removeMessageListener(ClientHandler listener) {
        messageListener.remove(listener);
    }

    public boolean isExist (ClientHandler listener) {
   	 return messageListener.contains(listener);
   }
   */
    
    

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    public boolean isExist (MessageListener listener) {
   	 return messageListeners.contains(listener);
   }
}
