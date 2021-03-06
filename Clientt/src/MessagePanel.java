package Clientt.src;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MessagePanel extends javax.swing.JPanel  implements MessageListener  {

    /**
     * Creates new form MessagePanel
     */
    public MessagePanel() {
        initComponents();
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>(listModel);
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(500, 600));

        jTextField1.setPreferredSize(new java.awt.Dimension(500, 30));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jList2);

        jButton1.setText("Upload");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        jLabel1.setText("Chatting with:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(433, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(jLabel1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            		 .addComponent(jLabel1)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 533, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(29, 29, 29))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jScrollPane2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24)))
        );
        
    	jTextField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = jTextField1.getText();
                    if(!text.isBlank()) {
                    client.msg(target, text);
                    listModel.addElement("You: " + text);
                    jTextField1.setText("");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }// </editor-fold>                        

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    	  try {
              JFileChooser chooser = new JFileChooser();
              FileNameExtensionFilter filter = new FileNameExtensionFilter(
            	        "JPG & GIF Images", "jpg", "gif", "txt");
              chooser.setFileFilter(filter);
              
              int c = chooser.showOpenDialog(this);
              if (c == JFileChooser.APPROVE_OPTION) {
            	  JOptionPane.showMessageDialog(this, "Sending " +
            	            chooser.getSelectedFile().getName());
                  File myFile = chooser.getSelectedFile();
                  byte [] mybytearray  = new byte [(int)myFile.length()];
                  FileInputStream fis = new FileInputStream(myFile);
                  BufferedInputStream bis = new BufferedInputStream(fis);
                  bis.read(mybytearray,0,mybytearray.length); //Write file byte to mybytearray
                  client.sendFile(mybytearray, target, chooser.getSelectedFile().getName());
                  listModel.addElement("Send file: " + chooser.getSelectedFile().getName());
                 // OutputStream os = sock.getOutputStream();
                  System.out.println("Sending " + chooser.getSelectedFile().getName() + "(" + (int)myFile.length() + " bytes)");
                //  os.write(mybytearray,0,mybytearray.length);
                //  os.flush();
                  System.out.println("Done.");
                  
                  
                  
                  /*FileInputStream in = new FileInputStream(f);
                  byte b[] = new byte[in.available()];
                  in.read(b);
                  Data data = new Data();
                  data.setStatus(jComboBox1.getSelectedItem() + "");
                  data.setName(txtName.getText().trim());
                  data.setFile(b);
                  out.writeObject(data);
                  out.flush();
                  txt.append("send 1 file ../n");*/
              }
          } catch (Exception e) {
              JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
          }
          // TODO add your handling code here:
      }//GEN-LAST:event_jButton2ActionPerformed
    
    
    public void setCL(ChatClient client, String login) {
    	this.client = client;
    	this.target = login;
    }
    
    public void run() {
    	if(this.client.isExist(this)) {
    		this.client.removeMessageListener(this);
    	}
    	this.client.addMessageListener(this);
    	listModel.removeAllElements();
    	 jLabel1.setText("Chatting with: " + target);
    	

    }
    
    @Override
    public void onMessage(String fromLogin, String msgBody) {
    	System.out.println("Recevied message");
        if (target.equalsIgnoreCase(fromLogin)) {
            String line = fromLogin + ": " + msgBody;
            listModel.addElement(line);
        }
    }
    
    /*
    
    @Override
    public void online(String login) {
       
    }

    @Override
    public void offline(String login) {
       
    }*/
    
    private ChatClient client;
    private String target;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration                   
}


    
                   



