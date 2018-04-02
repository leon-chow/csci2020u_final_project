package src.sample;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.event.ActionEvent;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.control.TextArea;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.sql.Time;


public class Main extends Application {
    Boolean send;
    public static Stage mainStage;
    String temp;

    public static Pane mainMenu = new Pane();
    public static Pane optionsMenu = new Pane();

    public static Scene optionsScene = new Scene(optionsMenu);
    public static Scene menuScene = new Scene(mainMenu);

    public static Parent parentFightScene;
    public static Parent parentVolumeScene;

    public static ColorAdjust colorAdjust = new ColorAdjust();

    public static BufferedReader br;
    File helpFile = new File("src/sample/Instructions.txt");
    File creditsFile = new File("src/sample/Credits.txt");

    @FXML Button btnGoBack = new Button();
    @FXML public Slider volumeSlider;
    @FXML public Slider brightnessSlider;

    @FXML
    ImageView picoloStanding = new ImageView();
    @FXML
    ImageView picoloKiblast = new ImageView();
    @FXML
    javafx.scene.control.TextField txtTypeMsg = new TextField(); //using the ID of of the messaging field
    @FXML
    TextArea txtChatBox = new TextArea(); //using the ID of the chatbox field
    @FXML
    ImageView gokustanding = new ImageView();
    @FXML
    ImageView gokuattack = new ImageView();
    @FXML
    ImageView gokukiblast = new ImageView();
    @FXML
    ImageView Naruto = new ImageView();
    final Float playerHPValue = 1.0f;
    final Float enemyHPValue = 1.0f;

    @FXML
    ProgressBar playerHPProgress = new ProgressBar(playerHPValue);
    @FXML ProgressBar enemyHPProgress = new ProgressBar(enemyHPValue);


    @FXML
    private TextArea txtRules;
    private static MediaPlayer mp;
    private MediaView mv;
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fight.fxml"));
        FXMLLoader volumeLoader = new FXMLLoader(getClass().getResource("Volume.fxml"));

        Parent root = (Parent)loader.load();
        Parent volume = (Parent)volumeLoader.load();

        parentFightScene = root;
        parentVolumeScene = volume;

        //loaded file in txtRules
        txtRules = new TextArea();
        txtRules.setStyle("-fx-control-inner-background:blue; -fx-opacity: transparent");
        txtRules.setPrefSize(300,300);
        txtRules.setDisable(true);
        primaryStage.setResizable(false); //doesn't let you resize window

        mainMenu.setPrefSize(603, 400);
        optionsMenu.setPrefSize(603, 400);

        Scene playScene = new Scene(root, 603, 400);
        Scene volumeScene = new Scene(volume, 603, 400);

        try {
            Media audo = new Media(getClass().getResource("music.mp3").toURI().toString());
            mp = new MediaPlayer(audo);
           // mv.setMediaPlayer(mp);
            mp.play();
            mp.setRate(1);
            mp.setVolume(0.1);
            mp.getVolume();

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
        MenuItem Credits = new MenuItem("Credits");
        MenuItem goBack = new MenuItem("Go Back");

        MenuBox menuBox = new MenuBox(Create, Play, Options, Exit);
        MenuBox optionsBox = new MenuBox(Help, Volume, Credits);
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

            Thread th = new Thread(() -> {
                /*src.sample.Client charlie;
                charlie = new src.sample.Client("127.0.0.1");
                charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                charlie.startRunning(); */
                Server test = new Server();
                test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                test.startRunning();
                //sendMessage();
            });
            th.setDaemon(true);
            th.start();
            mp.setVolume(0.05);
        });


        Help.setOnMouseClicked((MouseEvent e) -> {
            try {
                txtRules.clear();
                txtRules.setWrapText(true);
                br = new BufferedReader(new FileReader(helpFile));
                String next = null;
                if (txtRules.getLength() <= 0) {
                    txtRules.appendText("How to play: \n \n");
                    while ((next = br.readLine()) != null) {
                        txtRules.appendText(next);
                    }
                }

                txtRules.setVisible(true);
                optionsMenu.getChildren().add(txtRules);

                txtRules.setTranslateX(10);
                txtRules.setTranslateY(50);
                br.close();
            } catch (FileNotFoundException error) {
                error.printStackTrace();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });

        Credits.setOnMouseClicked(e -> {
            try {
                txtRules.clear();
                txtRules.setWrapText(true);
                br = new BufferedReader(new FileReader(creditsFile));
                String next = "";
                if (txtRules.getLength() <= 0) {
                    while ((next = br.readLine()) != null) {
                        txtRules.appendText(next + "\n");
                    }
                }

                txtRules.setVisible(true);
                optionsMenu.getChildren().add(txtRules);

                txtRules.setTranslateX(10);
                txtRules.setTranslateY(50);
                br.close();
            } catch (FileNotFoundException error) {
                error.printStackTrace();
            } catch (IOException error) {
                error.printStackTrace();
            }
        });

        Volume.setOnMouseClicked(e -> {
            txtRules.setVisible(false);
            primaryStage.setScene(volumeScene);
        });

        primaryStage.setTitle("DragonBall Ghetto");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public void sliderDragDetected (MouseEvent mouseEvent) {
        volumeSlider.setValue(mp.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

    public void brightnessMouseDrag (MouseEvent mouseEvent) {
        double brightness = brightnessSlider.getValue();
        colorAdjust.setBrightness(brightness);
        parentFightScene.setEffect(colorAdjust);
        parentVolumeScene.setEffect(colorAdjust);
        mainMenu.setEffect(colorAdjust);
        optionsMenu.setEffect(colorAdjust);
    }

    public void backOnAction(ActionEvent actionEvent) {
        System.out.println("Testing...");
        mainStage.setScene(optionsScene);
    }

    public void punchOnAction(ActionEvent actionEvent) {
        //punch button when clicked
        gokuattack.setVisible(true);
        gokustanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2.64),
                ae -> gokustanding.setVisible(true)));
        timeline.play();


        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(2.64),
                ae -> gokuattack.setVisible(false)));
        timeline1.play();

        enemyHPProgress.setProgress(enemyHPProgress.getProgress() - .1);
        System.out.println("Punch");
    }

    public void kiblastOnAction(ActionEvent actionEvent) {

        //punch button when clicked
        gokukiblast.setVisible(true);
        gokustanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                ae -> gokustanding.setVisible(true)));
        timeline.play();


        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(1.00),
                ae ->  gokukiblast.setVisible(false)));
        timeline1.play();

        enemyHPProgress.setProgress(enemyHPProgress.getProgress() - .1);
        System.out.println("Punch");


        System.out.println("Ki Blast");
    }

    public void kickOnAction(ActionEvent actionEvent) {
        //punch button when clicked
        Naruto.setVisible(true);
        gokustanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
                ae -> gokustanding.setVisible(true)));
        timeline.play();


        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(2),
                ae ->  Naruto.setVisible(false)));
        timeline1.play();

        enemyHPProgress.setProgress(enemyHPProgress.getProgress() - .1);


        System.out.println("Ki Blast");
    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException { //send button when clicked
        //sendMessage(); //calls send message function
    }

    public void onSendEnter(KeyEvent keyEvent) throws IOException { //when you hit enter while in message field
        if (keyEvent.getCode().toString().equals("ENTER")){
            //sendMessage(); //calls send message function
        }
    }

    public void picKiblastOnAaction(ActionEvent actionEvent) {
        pickiblast();
    }

    public void pickiblast(){
        picoloKiblast.setVisible(true);
        picoloStanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1.64),
                ae -> picoloStanding.setVisible(true)));
        timeline.play();


        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(1.64),
                ae -> picoloKiblast.setVisible(false)));
        timeline1.play();

        playerHPProgress.setProgress(playerHPProgress.getProgress() - .1);
        System.out.println("Picolo KiBlast");
    }

    public void picPunchOnAction(ActionEvent actionEvent) {
    }

    public void picKickOnAction(ActionEvent actionEvent) {
    }
//TEHSEENS SERVER CODE, DO NOT USE
    /*
    public void client() throws IOException {
        if (send = Boolean.TRUE) {
            Socket socket = new Socket("192.168.1.146", 8090);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String request = temp;
            out.print(request);
            out.flush();
            txtChatBox.setWrapText(true); //wraps to next line if message is too long
            txtChatBox.appendText(request + "\n"); //appends the message with a next line at the end of it
        }
    }

    public void Server() throws IOException {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(8195);
                Socket clientSocket = serverSocket.accept();

                InputStream inStream = clientSocket.getInputStream();
                InputStreamReader reader = new InputStreamReader(inStream);
                BufferedReader in = new BufferedReader(reader);

                String line = null;
                while ((line = in.readLine()) != null) {
                    // do something with 'line'
                    System.out.println(line);
                }

            } catch (IOException e1) {
                System.out.println("Client Disconnected");
                e1.printStackTrace();


            }
            //... input and output goes here ...
        }
    }
*/



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