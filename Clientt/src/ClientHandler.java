package Clientt.src;
public interface ClientHandler {
	 public void onMessage(String fromLogin, String msgBody);
	 public void online(String login);
	 public void offline(String login);

}
