package Clientt.src;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

public class UserListPanel extends javax.swing.JPanel implements UserStatusListener {
    private DefaultListModel<String> userListModel;
    private javax.swing.JList<String> jList1;

    /**
     * Creates new form UserListPanel
     */
	   @Override
	    public void online(String login) {
	        userListModel.addElement(login);
	    }

	    @Override
	    public void offline(String login) {
	        userListModel.removeElement(login);
	    }
	    public UserListPanel( ChatClient client) {
	        
	        
	        userListModel = new DefaultListModel<>();
	        jList1 = new javax.swing.JList<>(userListModel);
	        
	        initComponents();
	        
	        this.client = client;
	        this.client.addUserStatusListener(this);
	        this.setVisible(true);
	      /*  try {
	        this.client.login("t", "t");
	        }catch(IOException e) {
	        	e.printStackTrace();
	        }*/
	       // this.client.getList();
	       // this.MessagePanel = MessagePanel;
	        
	    }
	    
    public UserListPanel(MessagePanel MPanel, ChatClient client, String name) {
    	//MPanel = new MessagePanel();
    	
        
        
        userListModel = new DefaultListModel<>();
        jList1 = new javax.swing.JList<>(userListModel);
        this.MessagePanel = MPanel;
        
        
        initComponents();
        
        this.client = client;
        this.login = name;
        this.client.addUserStatusListener(this);
        this.setVisible(true);
        
        this.client.getList(name);
       
       // this.client.getList();
       // this.MessagePanel = MessagePanel;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        
        //userListModel = new DefaultListModel<>();
       // jList1 = new javax.swing.JList<>(userListModel);
       

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 600));

       
        jList1.setPreferredSize(new java.awt.Dimension(300, 600));
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        
        
        
        
       
        
        jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
        		if(e.getClickCount() > 1) {
        			String login =  jList1.getSelectedValue();
        			MessagePanel.setCL(client, login);
        			MessagePanel.run();
        			
        			 MessagePanel.setVisible(true);
        			
        		}
        	}
        });
    }// </editor-fold>
    
    
    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8818);

       

        if (client.connect()) {
            try {
               
                UserListPanel userListPane = new UserListPanel(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);

                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.setVisible(true);
                client.login("tom4", "tom4");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
 


    // Variables declaration - do not modify                     
    

    private javax.swing.JScrollPane jScrollPane1;
    private MessagePanel MessagePanel;
    private ChatClient client;
    private String login;
  
    // End of variables declaration                   
}
