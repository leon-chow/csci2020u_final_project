package sample;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.image.Image;
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
import java.io.File;


public class Main extends Application {
    private TextArea txtRules;
    private MediaPlayer mp;
    @Override
    public void start(Stage primaryStage) throws Exception {
        txtRules = new TextArea();
        txtRules.setStyle("-fx-control-inner-background:blue; -fx-opacity: transparent");
        txtRules.setDisable(true);
        txtRules.setText("How to Play:\n\nYou and other players will take turns to attack. By utilizing different abilities and" +
                " attacks,\n you can deal a significant amount of Health Points (HP). The last player who has not have their HP reduced\n to 0" +
                " will win.");

        Pane mainMenu = new Pane();
        mainMenu.setPrefSize(860, 600);

        Pane optionsMenu = new Pane();
        optionsMenu.setPrefSize(860, 600);

        Scene optionsScene = new Scene(optionsMenu);
        Scene menuScene = new Scene(mainMenu);


        try {
            AudioClip audo = new AudioClip(getClass().getResource("music.mp3").toURI().toString());
            audo.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        try (InputStream is = Files.newInputStream(Paths.get("res/dbz.jpg"))) {
            ImageView img = new ImageView(new Image(is));
            img.setFitWidth(860);
            img.setFitHeight(600);
            mainMenu.getChildren().add(img);
            is.close();
        }


        try (InputStream os = Files.newInputStream(Paths.get("res/lol.jpg"))) {
            ImageView img1 = new ImageView(new Image(os));
            img1.setFitWidth(860);
            img1.setFitHeight(600);
            optionsMenu.getChildren().add(img1);
            os.close();
        } catch (IOException x) {
            System.out.println("Failed");
        }

        MenuItem Create = new MenuItem("Create New Character");
        MenuItem ServerList = new MenuItem("ServerList");
        MenuItem Options = new MenuItem("Options");
        MenuItem Exit = new MenuItem("Exit");

        MenuItem Help = new MenuItem("How to play");
        MenuItem Volume = new MenuItem("Volume");
        MenuItem Brightness = new MenuItem("Brightness");
        MenuItem Effects = new MenuItem("Effects");
        MenuItem goBack = new MenuItem("Go Back");

        MenuBox menuBox = new MenuBox(Create, ServerList, Options, Exit);
        MenuBox optionsBox = new MenuBox(Help, Volume, Brightness, Effects);

        menuBox.setTranslateX(600);
        menuBox.setTranslateY(300);
        optionsBox.setTranslateX(600);
        optionsBox.setTranslateY(300);
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

        Help.setOnMouseClicked(e -> {
            txtRules.setVisible(true);
            optionsMenu.getChildren().add(txtRules);
            txtRules.setTranslateX(10);
            txtRules.setTranslateY(200);
        });



        Volume.setOnMouseClicked(e -> {
            txtRules.setVisible(true);
            optionsMenu.getChildren().add(txtRules);
            txtRules.setTranslateX(10);
            txtRules.setTranslateY(200);
        });

        primaryStage.setTitle("Final Project");
        primaryStage.setScene(menuScene);
        primaryStage.show();
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
            sep.setEndX(200);
            sep.setStroke(Color.GREY);
            return sep;
        }
    }

    private static class MenuItem extends StackPane {
        public MenuItem(String name) {

            Rectangle bg = new Rectangle(200, 30);
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