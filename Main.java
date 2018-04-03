//Leon Chow 100617197 Samatar Mumin 100637553 Tehseen Chaudhry 100618539 Bevan Donbosco 100618701
//This program opens a game menu where the user can play the game. The user can learn how to play by
//opening up the options menu. They can change the volume and the brightness of the game too.
//When either player 1 or player 2 reaches 0 HP, the game ends and the user exits the client
package src.sample;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
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

//main function
public class Main extends Application{
    //variables and scenes to be used in the program
    Boolean send;
    public static Stage mainStage;

    public float damage;

    public static int turnCounter = 0;

    public static Pane mainMenu = new Pane();
    public static Pane optionsMenu = new Pane();

    public static Scene optionsScene = new Scene(optionsMenu);
    public static Scene menuScene = new Scene(mainMenu);
    
    String temp;

    public static Parent parentFightScene;
    public static Parent parentVolumeScene;
    public static Parent parentGameOver;

    public static Scene globalGameOverScene;

    public static ColorAdjust colorAdjust = new ColorAdjust();

    public static BufferedReader br;
    //files to be read through input
    File helpFile = new File("src/sample/Instructions.txt");
    File creditsFile = new File("src/sample/Credits.txt");
    File gameLengths = new File("src/sample/GameLengths.txt");

    //images and gifs to be loaded in the scenes
    @FXML
    ImageView SpecialBeam = new ImageView();
    @FXML
    ImageView picKick = new ImageView();
    @FXML
    ImageView gokustanding = new ImageView();
    @FXML
    ImageView picoloKiblast = new ImageView();
    @FXML
    ImageView picoloStanding = new ImageView();
    @FXML
    ImageView gokuattack = new ImageView();
    @FXML
    ImageView gokukiblast = new ImageView();
    @FXML
    ImageView Naruto = new ImageView();
    @FXML
    Button btnPicKiblast = new Button();
    @FXML
    Button btnPicPunch = new Button();
    @FXML
    Button btnPicKick = new Button();
    @FXML
    Button btnKick = new Button();
    @FXML
    Button btnPunch = new Button();
    @FXML
    Button btnKiblast = new Button();

    public Float playerHPValue = 1.0f;
    public Float enemyHPValue = 1.0f;

    //health bars and volume/brightness slider in the options menu
    @FXML
    ProgressBar playerHPProgress = new ProgressBar(playerHPValue);
    @FXML ProgressBar enemyHPProgress = new ProgressBar(enemyHPValue);
    @FXML Button btnGoBack = new Button();
    @FXML public Slider volumeSlider;
    @FXML public Slider brightnessSlider;

    @FXML
    private TextArea txtRules;
    private static MediaPlayer mp;
    private MediaView mv;
    @Override
    public void start(Stage primaryStage) throws Exception {
        //making the primary stage global
        mainStage = primaryStage;

        //loading in the stages to be used later
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fight.fxml"));
        FXMLLoader volumeLoader = new FXMLLoader(getClass().getResource("Volume.fxml"));
        FXMLLoader gameOverLoader = new FXMLLoader(getClass().getResource("GameOver.fxml"));

        //casting the loaders to be parents
        Parent root = (Parent)loader.load();
        Parent volume = (Parent)volumeLoader.load();
        Parent gameOver = (Parent)gameOverLoader.load();

        //making the loaders static global
        parentFightScene = root;
        parentVolumeScene = volume;
        parentGameOver = gameOver;

        //creating a textbox to be used to file read and show how to play and credits
        txtRules = new TextArea();
        txtRules.setStyle("-fx-control-inner-background:blue; -fx-opacity: transparent");
        txtRules.setPrefSize(300,300);
        txtRules.setDisable(true);
        primaryStage.setResizable(false); //doesn't let you resize window

        //changing size of window
        mainMenu.setPrefSize(603, 400);
        optionsMenu.setPrefSize(603, 400);

        //creating scenes to be used later
        Scene playScene = new Scene(root, 603, 400);
        Scene volumeScene = new Scene(volume, 603, 400);
        Scene gameOverScene = new Scene(gameOver, 603, 400);

        //set gameoverscene to global
        globalGameOverScene = gameOverScene;

        //loading in the music file that is played throughout the whole program
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

        //loading in the background for the main menu
        try (InputStream is = Files.newInputStream(Paths.get("res/dbz.jpg"))) {
            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(603);
            img.setFitHeight(400);
            mainMenu.getChildren().add(img);
            is.close();
        }

        //loading in the background for options menu
        try (InputStream os = Files.newInputStream(Paths.get("res/lol.jpg"))) {
            ImageView img1 = new ImageView(new Image(os));
            img1.setFitWidth(603);
            img1.setFitHeight(400);
            optionsMenu.getChildren().add(img1);
            os.close();
        } catch (IOException x) {
            System.out.println("Failed");
        }

        //creating main menu and buttons
        MenuItem Play = new MenuItem("Play");
        MenuItem Options = new MenuItem("Options");
        MenuItem Exit = new MenuItem("Exit");

        MenuItem Help = new MenuItem("How to play");
        MenuItem Volume = new MenuItem("Volume");
        MenuItem Credits = new MenuItem("Credits");
        MenuItem goBack = new MenuItem("Go Back");

        MenuBox menuBox = new MenuBox(Play, Options, Exit);
        MenuBox optionsBox = new MenuBox(Help, Volume, Credits);
        MenuBox volumeBox = new MenuBox(goBack);

        //setting coordinates for items to be placed
        menuBox.setTranslateX(350);
        menuBox.setTranslateY(200);
        optionsBox.setTranslateX(350);
        optionsBox.setTranslateY(200);
        goBack.setTranslateX(10);
        goBack.setTranslateY(10);


        //adds the items to the menus
        mainMenu.getChildren().addAll(menuBox);
        optionsMenu.getChildren().addAll(optionsBox, goBack);


        //event handlers with lambda functions when buttons are clicked
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

            //changes scene of primary stage to play
            primaryStage.setScene(playScene);

            //creates thread for server
            Thread th = new Thread(() -> {
                Server test = new Server();
                test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                test.startRunning();
                //sendMessage();
            });
            th.setDaemon(true);
            th.start();

            //creates thread for client
            Thread ts = new Thread(() -> {
                Client client;
                client = new Client("10.190.39.176");
                client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                client.startRunning();
            });

            ts.setDaemon(true);

            //sets delay for the volume
            Timeline timeline1 = new Timeline(new KeyFrame(
                    Duration.seconds(12.00),
                    ae ->  ts.start()));
            timeline1.play();
            mp.setVolume(0.05);


        });


        //function to output txt file with the rules
        Help.setOnMouseClicked((MouseEvent e) -> {
            try {
                txtRules.clear();
                txtRules.setWrapText(true);
                br = new BufferedReader(new FileReader(helpFile));
                String next = null;
                //if the file is still reading
                if (txtRules.getLength() <= 0) {
                    txtRules.appendText("How to play: \n \n");
                    while ((next = br.readLine()) != null) {
                        txtRules.appendText(next);
                    }
                }

                //shows box when user clicks on help
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

        //function to read the credits from the text file
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

        //sets the volume stage, hides text area
        Volume.setOnMouseClicked(e -> {
            txtRules.setVisible(false);
            primaryStage.setScene(volumeScene);
        });

        primaryStage.setTitle("DragonBall");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    //function to adjust the volume when the slider is moved
    public void sliderDragDetected (MouseEvent mouseEvent) {
        volumeSlider.setValue(mp.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

    //function to adjust the brightness of all scenes when the slider is moved
    public void brightnessMouseDrag (MouseEvent mouseEvent) {
        double brightness = brightnessSlider.getValue();
        colorAdjust.setBrightness(brightness);
        parentFightScene.setEffect(colorAdjust);
        parentVolumeScene.setEffect(colorAdjust);
        mainMenu.setEffect(colorAdjust);
        optionsMenu.setEffect(colorAdjust);
    }

    //function to return to options menu
    public void backOnAction(ActionEvent actionEvent) {
        mainStage.setScene(optionsScene);
    }

    //function that exits the program
    public void backToMainMenu(ActionEvent actionEvent) {
        System.exit(0);
    }

    //function to enable the goku's buttons in the play stage when it is goku's turn
    public void setGokuTurn() throws IOException {

        btnPicKiblast.setDisable(true);
        btnPicKick.setDisable(true);
        btnPicPunch.setDisable(true);
        btnKiblast.setDisable(false);
        btnKick.setDisable(false);
        btnPunch.setDisable(false);
    }

    //function to enable piccolo's buttons when it is piccolo's turn
    public void setPicoloTurn() throws IOException {

        btnPicKiblast.setDisable(false);
        btnPicKick.setDisable(false);
        btnPicPunch.setDisable(false);
        btnKiblast.setDisable(true);
        btnKick.setDisable(true);
        btnPunch.setDisable(true);
    }

    //function to disable all buttons
    public void allDisable(){
        btnKiblast.setDisable(true);
        btnKick.setDisable(true);
        btnPunch.setDisable(true);
        btnPicKiblast.setDisable(true);
        btnPicKick.setDisable(true);
        btnPicPunch.setDisable(true);
    }

    //function to create a generate a random number
    public float getRandom(){
        Random rand = new Random();
        float result = rand.nextFloat() * (0.5f - 0.0f) + 0.0f;

        return result;
    }


    //function to call punch method to process health bars and damage
    public void punchOnAction(ActionEvent actionEvent) throws IOException {
        //punch button when clicked
        setPicoloTurn();
        punch();
    }

    //function called by punchOnaction, which sets and hides animations and updates health bars
    public void punch() throws IOException {
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

        damage = getRandom();
        enemyHPValue -= damage;
        enemyHPProgress.setProgress(enemyHPValue);
        System.out.println("Special KameHameHa");
        checkGameOver();
    }

    //function that calls kiblast function and sets piccolo's turn to true
    public void kiblastOnAction(ActionEvent actionEvent) throws IOException {
        setPicoloTurn();
        kiblast();
    }

    //function called by kiblastOnAction, which sets and hides animations and updates health bars
    public void kiblast() throws IOException {
        gokukiblast.setVisible(true);
        gokustanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                ae -> gokustanding.setVisible(true)));
        timeline.play();

        //sets the delay for the animation
        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(1.00),
                ae ->  gokukiblast.setVisible(false)));
        timeline1.play();

        damage = getRandom();
        enemyHPValue -= damage;
        enemyHPProgress.setProgress(enemyHPValue);


        System.out.println("Ki Blast");
        checkGameOver();
    }


    //function to call kick function and set piccolo's turn to true
    public void kickOnAction(ActionEvent actionEvent) throws IOException {
        //punch button when clicked
        setPicoloTurn();
        kick();
    }

    //function called by kickOnAction, which sets and hides animations and updates health bars
    public void kick() throws IOException {
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

        damage = getRandom();
        enemyHPValue -= damage;
        enemyHPProgress.setProgress(enemyHPValue);

        System.out.println("Naruto!");
        checkGameOver();
    }

    public void sendOnAction(ActionEvent actionEvent) throws IOException { //send button when clicked
        //sendMessage(); //calls send message function
    }

    public void onSendEnter(KeyEvent keyEvent) throws IOException { //when you hit enter while in message field
        if (keyEvent.getCode().toString().equals("ENTER")){
            //sendMessage(); //calls send message function
        }
    }

    //function to call pickiblast and sets goku's turn to true
    public void picKiblastOnAaction(ActionEvent actionEvent) throws IOException {
        setGokuTurn();
        pickiblast();
    }

    //function called by pickiblast, which sets and hides animations and updates health bars
    public void pickiblast() throws IOException {
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

        damage = getRandom();
        playerHPValue -= damage;
        playerHPProgress.setProgress(playerHPValue);
        System.out.println("Picolo KiBlast");
        checkGameOver();
    }

    //function to call picPunch and sets goku's turn to true
    public void picPunchOnAction(ActionEvent actionEvent) throws IOException {
        setGokuTurn();
        picPunch();
    }

    //function called by picPunch, which sets and hides animations and updates health bars
    public void picPunch() throws IOException {
        SpecialBeam.setVisible(true);
        picoloStanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1.64),
                ae -> picoloStanding.setVisible(true)));
        timeline.play();


        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(1.64),
                ae -> SpecialBeam.setVisible(false)));
        timeline1.play();

        damage = getRandom();
        playerHPValue -= damage;
        playerHPProgress.setProgress(playerHPValue);
        System.out.println("Picolo Special Beam Cannon");
        checkGameOver();
    }

    //function to call picKick and set Goku's turn to true
    public void picKickOnAction(ActionEvent actionEvent) throws IOException {
        setGokuTurn();
        picKick();
    }

    //function called by picKick, which sets and hides animations and updates health bars
    public void picKick() throws IOException {
        picKick.setVisible(true);
        picoloStanding.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1.64),
                ae -> picoloStanding.setVisible(true)));
        timeline.play();


        Timeline timeline1 = new Timeline(new KeyFrame(
                Duration.seconds(1.64),
                ae ->  picKick.setVisible(false)));
        timeline1.play();

        damage = getRandom();
        playerHPValue -= damage;
        playerHPProgress.setProgress(playerHPValue);
        System.out.println("Picolo Kick");
        checkGameOver();
    }

    //checks to see if the game is over, which will output to the file how many turns it took for the battle, and sets stage to game over
    public void checkGameOver() throws IOException{
        if (playerHPValue <= 0 || enemyHPValue <= 0) {
            allDisable();
            System.out.println("Game Over");
            BufferedWriter bw = new BufferedWriter(new FileWriter(gameLengths, true));
            bw.append("Number of turns taken: " + turnCounter);
            bw.newLine();
            bw.flush();
            bw.close();
            turnCounter = 0;
            Timeline timeline5 = new Timeline(new KeyFrame(
                    Duration.seconds(4.00),
                    ae ->  mainStage.setScene(globalGameOverScene)));
            timeline5.play();
        }
        turnCounter++;
    }

    //function that appends the menu items to the box with seperators
    private static class MenuBox extends VBox {
        public MenuBox(MenuItem... items) {
            getChildren().add(createSeparator());
            for (MenuItem item : items) {
                getChildren().addAll(item, createSeparator());
            }
        }

        //function that creates menu seperators
        private Line createSeparator() {
            Line sep = new Line();
            sep.setEndX(235);
            sep.setStroke(Color.GREY);
            return sep;
        }
    }

    //function that creates menu items and styles them up
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