//Leon Chow 100617197 Samatar Mumin 100637553 Tehseen Chaudhry 100618539 Bevan Donbosco 100618701

package src.sample;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


//class to handle client connection for chat
public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String ServerIP;
    public Socket connection;

    //client constructor to send messages and handle chat
    public Client(String host){
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

    //connects to the server
    public void startRunning(){
        try{
            connectToServer();
            setupStream();
            whileChatting();
        } catch(EOFException eofException){
            showMessage("\n cilent ended session");
        } catch(IOException ioException){
            ioException.printStackTrace();
        } finally {
            closeStream();
        }
    }
    //connecting server
    private  void connectToServer()throws IOException{
        showMessage("Attempting connection \n");
        connection = new Socket(InetAddress.getByName(ServerIP),6789);
        showMessage("Connected to "+connection.getInetAddress().getHostName());
    }

    //opens the stream to get input
    private void setupStream()throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n you are set up \n");
    }
    //while both the users are chatting
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
    //close the sockets and the stream
    private void closeStream(){
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
    //sends the messages to the server
    private void sendMessage(String message){
        try{
            output.writeObject("Cilent -"+message);
            output.flush();
            showMessage("\n cilent"+ message);
        }catch(IOException ioException){
            chatWindow.append("\n sending message error");
        }
    }

    //updating the chat window to show messages
    private void showMessage(final String m){
        SwingUtilities.invokeLater(

                new Runnable() {
                    public void run(){
                        chatWindow.append(m);
                    }
                }
        );
    }

    //allows the user to type
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
