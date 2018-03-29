

package sample;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Cilent extends JFrame {
    private JTextField userText;
    private  JTextArea chatWindow;
    private ObjectOutputStream output;
    private  ObjectInputStream input;
    private String message = "";
    private  String ServerIP;
    private Socket connection;

    public Cilent(String host){
        super("client");
        ServerIP= host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent event){
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText,BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow),BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);

    }

    //connecting to server
    public void startRunning(){
        try{
            connectToServer();
            setupStream();
            whileChatting();
        }catch(EOFException eofException){
            showMessage("\n cilent ended session");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally {
            closeCrap();
        }
    }
    //connecting server
    private  void connectToServer()throws IOException{
        showMessage("Attempting connection \n");
        connection = new Socket(InetAddress.getByName(ServerIP),6789);
        showMessage("Connected to "+connection.getInetAddress().getHostName());

    }

    //setup streams to get msgs
    private void setupStream()throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n you are set up \n");
    }
    //while chatting
    private void whileChatting()throws IOException{
        ableToType(true);
        do{
            try{
                    message= (String) input.readObject();
                    showMessage("\n" + message);
            }catch (ClassNotFoundException classNotfoundException){
                showMessage("\n  unsure");
            }

        }while(!message.equals("SERVER - END"));
    }
    //close sockets and stream
    private void closeCrap(){
        showMessage("\n closes stream/sockets");
        ableToType(false);
        try{
                output.close();
                input.close();
                connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    //send messages server
    private void sendMessage(String message){
        try{
            output.writeObject("Cilent -"+message);
            output.flush();
            showMessage("\n cilent"+ message);
        }catch(IOException ioException){
            chatWindow.append("\n sending message error");
        }
    }

    //changing chat window
    private void showMessage(final String m){
        SwingUtilities.invokeLater(

                new Runnable() {
                    public void run(){
                        chatWindow.append(m);
                    }
                }
        );
    }

    //abletoptype
    private  void ableToType(final boolean tof){
        SwingUtilities.invokeLater(

                new Runnable() {
                    public void run(){
                        userText.setEditable(tof);
                    }
                }
        );

    }
}
