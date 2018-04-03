//Leon Chow 100617197 Samatar Mumin 100637553 Tehseen Chaudhry 100618539 Bevan Donbosco 100618701
package src.sample;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//class that handles the server connection with the client and enables chatting between both
public class Server extends JFrame {

    //variables used later
    private TextField userText;
    private TextArea chat;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ServerSocket serverSocket;
    private Socket connectionSocket;

    //server constructor, handles messenger
    public Server(){
        super("Dragon Ball Messenger");
        userText = new TextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event){
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText,BorderLayout.NORTH);
        chat = new TextArea();
        add(new JScrollPane(chat));
        setSize(300,150);
        setVisible(true);

    }
    //handles the serer connection
    public void startRunning(){
        try{
            serverSocket = new ServerSocket(6789,100);
            while(true) {
                try {
                    WaitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eofExpection) {
                    showMessage("\n Server Ended the connection");
                }finally{
                    closeStream();
                }
            }
    } catch (IOException ioException){
        ioException.printStackTrace();
        }
    }

    //awaiting for connection from the client
    private void WaitForConnection() throws IOException {
        showMessage("waiting for other connection");
        connectionSocket = serverSocket.accept();
        showMessage("now connected to"+ connectionSocket.getInetAddress().getHostName());
    }
    //sets up streams for input and output
    private void setupStreams() throws IOException{
        outputStream= new ObjectOutputStream(connectionSocket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(connectionSocket.getInputStream());
        showMessage("stream setup");
    }

    //while the client and servers are open and connected
    private void whileChatting()throws IOException{
        String message = " successfully connected";
        sendMessage(message);
        ableToType(true);
        do{
            try {
                message = (String) inputStream.readObject();
                showMessage("\n" +  message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n user sending error");
            }

        }while(!message.equals("CILENT - END"));
    }

    //closes streams after the connections are ended
    private void closeStream(){
        showMessage("\n closing connection \n");
        ableToType(false);
        try {
            outputStream.close();
            inputStream.close();
            connectionSocket.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //handles messages between client and user
    private void sendMessage(String message){
        try{
            outputStream.writeObject("Username -" + message);
            outputStream.flush();
            showMessage("\n Username - " + message);
        }catch(IOException ioException){
            chat.append(" \n error, cant send message");
        }

    }

    //updates the chat for both user and client
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        chat.append(text);
                    }
                }
        );
    }

    //allowing users to type
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        userText.setEditable(tof);
                    }
                }
        );
    }
}
