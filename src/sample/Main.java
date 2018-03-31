package src.sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.event.ActionEvent;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.control.TextArea;

import javafx.scene.media.MediaPlayer;

import javax.swing.*;


public class Main extends Application {
    Boolean send;
    public static Stage mainStage;
    String temp;

    public static Pane mainMenu = new Pane();

    public static Pane optionsMenu = new Pane();

    public static Scene optionsScene = new Scene(optionsMenu);
    public static Scene menuScene = new Scene(mainMenu);

    @FXML
    javafx.scene.control.TextField txtTypeMsg = new TextField(); //using the ID of of the messaging field

    @FXML
    TextArea txtChatBox = new TextArea(); //using the ID of the chatbox field
    private TextArea txtRules;
    private MediaPlayer mp;

    @FXML
    private Button btnGoBack = new Button();

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;

        mainMenu.setPrefSize(603, 400);
        optionsMenu.setPrefSize(603, 400);

        Parent parentFight = FXMLLoader.load(getClass().getResource("Fight.fxml"));
        Parent parentVolume = FXMLLoader.load(getClass().getResource("Volume.fxml"));

        Scene playScene = new Scene(parentFight, 603, 400);
        Scene volumeScene = new Scene(parentVolume, 603, 400);

        txtRules = new TextArea();
        txtRules.setStyle("-fx-control-inner-background:blue; -fx-opacity: transparent");
        txtRules.setDisable(true);
        txtRules.setText("How to Play:\n\nYou and other players will take turns to attack. By utilizing different abilities and" +
                " attacks,\n you can deal a significant amount of Health Points (HP). The last player who has not have their HP reduced\n to 0" +
                " will win.");
        primaryStage.setResizable(false); //doesn't let you resize window

        try {
            AudioClip audo = new AudioClip(getClass().getResource("music.mp3").toURI().toString());
            audo.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        try (InputStream is = Files.newInputStream(Paths.get("res/dbz.jpg"))) {
            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(603);
            img.setFitHeight(400);
            mainMenu.getChildren().add(img);
            is.close();
        }


        try (InputStream os = Files.newInputStream(Paths.get("res/lol.jpg"))) {
            ImageView img1 = new ImageView(new Image(os));
            img1.setFitWidth(603);
            img1.setFitHeight(400);
            optionsMenu.getChildren().add(img1);
            os.close();
        } catch (IOException x) {
            System.out.println("Failed");
        }

        MenuItem Create = new MenuItem("Create New Character");
        MenuItem Play = new MenuItem("Play");
        MenuItem Options = new MenuItem("Options");
        MenuItem Exit = new MenuItem("Exit");

        MenuItem Help = new MenuItem("How to play");
        MenuItem Volume = new MenuItem("Volume");
        MenuItem Brightness = new MenuItem("Brightness");
        MenuItem Effects = new MenuItem("Effects");
        MenuItem goBack = new MenuItem("Go Back");

        MenuBox menuBox = new MenuBox(Create, Play, Options, Exit);
        MenuBox optionsBox = new MenuBox(Help, Volume, Brightness, Effects);
        MenuBox volumeBox = new MenuBox(goBack);

        menuBox.setTranslateX(350);
        menuBox.setTranslateY(200);
        optionsBox.setTranslateX(350);
        optionsBox.setTranslateY(200);
        goBack.setTranslateX(10);
        goBack.setTranslateY(10);

        mainMenu.getChildren().addAll(menuBox);
        optionsMenu.getChildren().addAll(optionsBox, goBack);


        Exit.setOnMouseClicked(e -> {
            System.exit(0);
        });

        goBack.setOnMouseClicked(e -> {
            txtRules.setVisible(false);
            primaryStage.setScene(menuScene);
        });

        Options.setOnMouseClicked(e -> {
            primaryStage.setScene(optionsScene);
        });

        Play.setOnMouseClicked(e -> {
            primaryStage.setScene(playScene);
            //Server test = new Server();
            //test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //test.startRunning();
            //Server();
        });

        Help.setOnMouseClicked(e -> {
            txtRules.setVisible(true);
            optionsMenu.getChildren().add(txtRules);
            txtRules.setTranslateX(10);
            txtRules.setTranslateY(200);
        });


        Volume.setOnMouseClicked(e -> {
            primaryStage.setScene(volumeScene);
        });

        btnGoBack.setOnAction(e -> {
            primaryStage.setScene(optionsScene);
        });


        primaryStage.setTitle("DragonBall Ghetto");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public void backOnAction(ActionEvent actionEvent) {
        mainStage.setScene(optionsScene);
        System.out.println("Heading back");
    }

    public void punchOnAction(ActionEvent actionEvent) { //punch button when clicked
        System.out.println("Punch");
    }

    public void kiblastOnAction(ActionEvent actionEvent) { //ki blast button when clicked
        System.out.println("Ki Blast");
    }

    public void kickOnAction(ActionEvent actionEvent) { //kick button when clicked
        System.out.println("Kick");
    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException { //send button when clicked
        sendMessage(); //calls send message function
    }

    public void onSendEnter(KeyEvent keyEvent) throws IOException { //when you hit enter while in message field
        if (keyEvent.getCode().toString().equals("ENTER")){
            sendMessage(); //calls send message function
        }
    }



    public void client() throws IOException {
        if (send = Boolean.TRUE) {
            Socket socket = new Socket("192.168.1.172", 8195);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String request = temp;
            out.print(temp);
            out.flush();
            System.out.println("hello");
            socket.close();
            txtChatBox.setWrapText(true); //wraps to next line if message is too long
            txtChatBox.appendText(request + "\n"); //appends the message with a next line at the end of it
        }
    }

    /*public void Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8095);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                InputStream inStream = clientSocket.getInputStream();
                InputStreamReader reader = new InputStreamReader(inStream);
                BufferedReader in = new BufferedReader(reader);

                String line = null;
                while ((line = in.readLine()) != null) {
                    // do something with 'line'
                    System.out.println(line);
                }

            } catch (IOException e) {
                System.out.println("Client Disconnected");
                e.printStackTrace();

            }
            //... input and output goes here ...
        }
    }*/


    public void sendMessage() throws IOException {
        temp = txtTypeMsg.getText(); //temp is the whatever is in the text field
        txtTypeMsg.clear(); //clears message when it has been put into temp
        send = Boolean.TRUE;
        //client();
    }

    private static class MenuBox extends VBox {
        public MenuBox(MenuItem... items) {
            getChildren().add(createSeparator());
            for (MenuItem item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }

        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(235);
            sep.setStroke(Color.GREY);
            return sep;
        }
    }

    private static class MenuItem extends StackPane {
        public MenuItem(String name) {

            Rectangle bg = new Rectangle(235, 30);
            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Tw Cen MT Condensed", FontWeight.SEMI_BOLD, 22));
            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            setOnMousePressed(event -> {
                bg.setFill(Color.RED);
            });
            setOnMouseReleased(event -> {
                text.setFill(Color.WHITE);
                bg.setFill(Color.DARKGREY);
            });
            bg.setFill(Color.DARKGREY);
            text.setFill(Color.WHITE);
        }
    }
}