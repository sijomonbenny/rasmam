package aboullaite;

import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Arrays;

// Class to manage Client chat Box.
public class ChatClient {

    /** Chat client access */
 
    static class ChatAccess extends Observable {
        private Socket socket;
        private OutputStream outputStream;

        @Override
        public void notifyObservers(Object arg) {
            super.setChanged();
            super.notifyObservers(arg);
        }

        /** Create socket, and receiving thread */
        public void InitSocket(String server, int port) throws IOException {
            socket = new Socket(server, port);
            outputStream = socket.getOutputStream();

            Thread receivingThread = new Thread() {
                @Override
                public void run() {
                    try {
                             
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null)
                            notifyObservers(line);
                    } catch (IOException ex) {
                        notifyObservers(ex);
                    }
                }
            };
            receivingThread.start();
        }

        private static final String CRLF = "\r\n"; // newline

        /** Send a line of text */
        public void send(String text) {
            try {
                outputStream.write((text + CRLF).getBytes());
                outputStream.flush();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }

        /** Close the socket */
        public void close() {
            try {
                socket.close();
            } catch (IOException ex) {
                notifyObservers(ex);
            }
        }
    }

    /** Chat client UI */
    static class ChatFrame extends JFrame implements Observer {
  private javax.swing.JTextField inputTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextArea textArea;
    
        private ChatAccess chatAccess;

        public ChatFrame(ChatAccess chatAccess) {
            this.chatAccess = chatAccess;
            chatAccess.addObserver(this);
            buildGUI();
        }

        /** Builds the user interface */
        private void buildGUI() {                 
     jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inputTextField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Doubts Area");
        setFocusCycleRoot(false);
        setMinimumSize(new java.awt.Dimension(714, 360));
        setPreferredSize(null);
        getContentPane().setLayout(null);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        jList1.setBackground(new java.awt.Color(46, 17, 75));
        jList1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jList1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jList1.setForeground(new java.awt.Color(255, 255, 255));        
        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 190, 230, 130);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Active Participants");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 170, 130, 14);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel2.setText("Home");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel2);
        jLabel2.setBounds(100, 140, 32, 14);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aboullaite/homescreen.jpg"))); // NOI18N
        jLabel3.setText("jLabel3");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(-10, -10, 240, 370);
        getContentPane().add(inputTextField);
        inputTextField.setBounds(230, 280, 400, 40);

        sendButton.setBackground(new java.awt.Color(46, 17, 75));
        sendButton.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        sendButton.setForeground(new java.awt.Color(255, 255, 255));
        sendButton.setText("Send");
        getContentPane().add(sendButton);
        sendButton.setBounds(630, 279, 60, 42);

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane2.setViewportView(textArea);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(230, 0, 460, 280);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 714, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 714, 360);

        pack();

            // Action for the inputTextField and the goButton
            ActionListener sendListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String str = inputTextField.getText();
                    if (str != null && str.trim().length() > 0)
                        chatAccess.send(str);
                    inputTextField.selectAll();
                    inputTextField.requestFocus();
                    inputTextField.setText("");
                }
            };
            inputTextField.addActionListener(sendListener);
            sendButton.addActionListener(sendListener);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    chatAccess.close();
                }
            });
        }
  private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {                                     
       this.setVisible(false);
        new Designer().setVisible(true);
        
    }        
        /** Updates the UI depending on the Object argument */
        public void update(Observable o, Object arg) {
            final Object finalArg = arg;
            
            String a[]=finalArg.toString().split("@");
            //System.out.println("Hiiiiiiiiiiiiiiii"+ChatClient.users);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {   
                    System.out.println("-----------------------------------");
                    ArrayList userList = new ArrayList();
                    String str = textArea.getText();
                    try {
                         str.replace(" ","");
                         String a[]=str.split("%");
                         for(int i=0;i<a.length;i++) {
                         if(i%2==0) { }
                         else {
                            str=a[i]+"\n";
                            userList.add(a[i]);
                            String[] stringArray = Arrays.copyOf(userList.toArray(), userList.toArray().length, String[].class);
                            jList1.setListData(stringArray);
                            System.out.println("User:"+str);
                            }	        
                        }
                    }
                    catch(StringIndexOutOfBoundsException se) {
                       
                    }
                    finally {
                     
                     textArea.append(finalArg.toString());
                     textArea.append("\n");
                    }
                   
                }
            });
        }
    }

    public static void main(String[] args) {
        
        
        String server = args[0];
        System.out.println("Server"+server);
        int port =2222;
        ChatAccess access = new ChatAccess();

        JFrame frame = new ChatFrame(access);
        
        frame.setTitle("RASMAM - Connected to " + server + ":" + port);
       // frame.setContentPane(new JLabel(new ImageIcon("C:\\Users\\SIJO\\Pictures\\Project Images\bg.jpeg")));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        //frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setVisible(true);

        try {
            access.InitSocket(server,port);
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }
    }
}