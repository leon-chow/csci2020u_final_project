package sample;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {


    private JTextField userText;
    private  JTextArea chatwindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    public Server(){
        super("instant messanger");
        userText = new JTextField();
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
        chatwindow = new JTextArea();
        add(new JScrollPane(chatwindow));
        setSize(300,150);
        setVisible(true);

    }
    //server
    public void startRunning(){
        try{
            server = new ServerSocket(6789,100);
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
    }catch(IOException ioException){
        ioException.printStackTrace();
        }

        //wait for connection, then display connection


    }

    private void WaitForConnection() throws IOException {
        showMessage("waiting for other conneection");
        connection = server.accept();
        showMessage("now connected to"+ connection.getInetAddress().getHostName());
    }
    private void setupStreams() throws IOException{
        output= new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("stream setup");
    }

    //while chatting

    private void whileChatting()throws IOException{
        String message = " successfully connected";
        sendMessage(message);
        ableToType(true);
        do{
            try {
                message = (String) input.readObject();
                showMessage("\n" +  message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n user sending error");
            }

        }while(!message.equals("CILENT - END"));
    }

    //clean up after chatting close stream/sockets
    private void closeStream(){
        showMessage("\n closing connection \n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }

        //send info to client
    }

    private void sendMessage(String message){
        try{
            output.writeObject("Username -" + message);
            output.flush();
            showMessage("\n Username - " + message);
        }catch(IOException ioException){
            chatwindow.append(" \n error, cant send message");
        }

    }

    //updates chat
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        chatwindow.append(text);
                    }
                }
        );
    }
    // letting the other player type
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
