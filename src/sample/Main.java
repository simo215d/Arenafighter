package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main extends Application {
    public static StackPane contentPaneHome = new StackPane();
    //set displayed content on screen
    public static void setContent(VBox vboxMenu, VBox vboxContent, ImageView imageView1){
        VBox vboxContentCurrent = new VBox(vboxMenu, vboxContent);
        contentPaneHome.getChildren().setAll(imageView1, vboxContentCurrent);
    }
    //input friends in friend list from database
    public static void loadFriendslist(VBox panel) throws SQLException {
        sqlCommands sql = new sqlCommands();
        //clear the list
        if (!panel.getChildren().isEmpty()){
            //clear
            panel.getChildren().clear();
        }
        //recreate the list
        ArrayList<HBox> friends = new ArrayList<>();
        ArrayList<String> names = sql.getFriendNames();
        ArrayList<String> statuses = sql.getFriendStatuses();
        for (int i = 0; i < names.size(); i++) {
            Label nameLabel = new Label(names.get(i));
            Label statusLabel = new Label(statuses.get(i));
            nameLabel.setStyle("-fx-text-fill: white");
            if (statuses.get(i).equals("Offline"))
            statusLabel.setStyle("-fx-text-fill: red");
            else statusLabel.setStyle("-fx-text-fill: limegreen");
            Label spacing = new Label(" - ");
            spacing.setStyle("-fx-text-fill: white");
            friends.add(new HBox(nameLabel, spacing,statusLabel));
        }
        for(HBox friend : friends){
            panel.getChildren().add(friend);
        }
        System.out.println("Friend list has been reloaded.");
        System.out.println("---/\\---");
    }
    public static void addFriendToDatabase(String name, VBox panel) throws SQLException{
        //call sql to add a row
        sqlCommands sql = new sqlCommands();
        String message = sql.addFriend(name);
        if (message.equals("error")) return;
        //reload the list
        loadFriendslist(panel);
    }
    public static void removeFriendFromDatabase(String name, VBox panel) throws SQLException{
        //call sql to remove a row
        sqlCommands sql = new sqlCommands();
        String message = sql.removeFriend(name);
        if (message.equals("error")) return;
        //reload the list
        loadFriendslist(panel);
    }
    //menu button backgrounds
    public final String menuButtonIdle = "-fx-background-color: transparent;"+"-fx-text-fill: white;"+"-fx-font-size: 15;";
    public final String menuButtonPressed = "-fx-background-color: transparent;"+"-fx-text-fill: gold;"+"-fx-font-size: 15;";
    //menu vbox backgrounds
    public final String menuVboxIdle = "-fx-background-color: transparent;"+"-fx-cursor: default";
    public final String menuVboxHover = "-fx-background-color: rgb(0,0,0,0.50);"+"-fx-cursor: hand";

    public void setSceneToGameOver(int i){
        System.out.println("we are in main class and are trying to swap to gameOver scene");
        System.out.println("players score was: "+i+" points");
        GameOver gameover = new GameOver();
        gameover.setScore(i);
        System.out.println("score is set");
        Scene gameoverScene = gameover.getScene();
        //this never worked
        //getMainStage().setScene(gameoverScene);
    }

    public static String currentScene = "client";

    @Override
    public void start(Stage clientStage) throws Exception{
        clientStage.initStyle(StageStyle.UNDECORATED);
        diabloGame dg = new diabloGame();
        clientStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (currentScene.equals("game")) {
                switch (event.getCode()) {
                    case W: dg.moveCameraW(); break;
                    case S: dg.moveCameraS(); break;
                    case D: dg.movePlayerRight(); break;
                    case A: dg.movePlayerLeft(); break;
                    case X: dg.moveCameraUp(); break;
                    case Z: dg.moveCameraDown(); break;
                    case P: dg.flyForwardCamera(); break;
                    case ESCAPE: System.out.println("escape was pressed and *gameover*"); dg.gameOver(); break;
                }
            }
        });

        //menu stuff
        Button menuItem1 = new Button("Play");
        Button menuItem2 = new Button("Home");
        Button menuItem3 = new Button("Profile");
        menuItem1.setPrefSize(75,75);
        menuItem2.setPrefSize(75,75);
        menuItem3.setPrefSize(75,75);
        menuItem1.setStyle(menuButtonIdle);
        menuItem2.setStyle(menuButtonIdle);
        menuItem3.setStyle(menuButtonIdle);
        menuItem1.setOnMousePressed(e-> menuItem1.setStyle(menuButtonPressed));
        menuItem1.setOnMouseReleased(e-> menuItem1.setStyle(menuButtonIdle));
        menuItem2.setOnMousePressed(e-> menuItem2.setStyle(menuButtonPressed));
        menuItem2.setOnMouseReleased(e-> menuItem2.setStyle(menuButtonIdle));
        menuItem3.setOnMousePressed(e-> menuItem3.setStyle(menuButtonPressed));
        menuItem3.setOnMouseReleased(e-> menuItem3.setStyle(menuButtonIdle));

        //play stuff
        Label labelPlay = new Label("Press the button to fight demons.");
        labelPlay.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white;");
        FileInputStream gamemodeInput = new FileInputStream("resources/diablo.png");
        Image gamemodeImage1 = new Image(gamemodeInput);
        ImageView gamemode1 = new ImageView(gamemodeImage1);
        gamemode1.setFitWidth(200);
        gamemode1.setFitHeight(150);
        Button enterHellButton = new Button("Enter Hell");
        String enterHellButtonIdle = "-fx-text-fill: white;"+"-fx-background-color: black;"+"-fx-border-width: 1;"+"-fx-border-color: white";
        String enterHellButtonHover = "-fx-text-fill: white;"+"-fx-background-color: darkred;"+"-fx-border-width: 1;"+"-fx-border-color: white";
        enterHellButton.setOnAction(actionEvent -> {
            currentScene="game";
            Scene gamescene = dg.getMainGroup();
            clientStage.setScene(gamescene);
        });

        enterHellButton.setStyle(enterHellButtonIdle);
        enterHellButton.setOnMouseEntered(mouseEvent -> {
            enterHellButton.setStyle(enterHellButtonHover);
        });
        enterHellButton.setOnMouseExited(mouseEvent -> {
            enterHellButton.setStyle(enterHellButtonIdle);
        });

        //home stuff
        FileInputStream landscapeinput = new FileInputStream("resources/landscape1.png");
        Image landscape = new Image(landscapeinput);
        ImageView imageView1 = new ImageView(landscape);
        imageView1.setFitWidth(600);
        imageView1.setFitHeight(400);
        Label labelHome = new Label("Homepage with news, such as patchnotes and updates.");
        labelHome.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white");
        FileInputStream wildriftInput = new FileInputStream("resources/wildrift.png");
        Image wildriftImage = new Image(wildriftInput);
        ImageView wildrift = new ImageView(wildriftImage);
        wildrift.setFitWidth(300);
        wildrift.setFitHeight(150);

        //profile + app buttons stuff
        FileInputStream profpic1input = new FileInputStream("resources/profpic1.png");
        Image profpic1 = new Image(profpic1input);
        ImageView profpic = new ImageView(profpic1);
        profpic.setFitWidth(30);
        profpic.setFitHeight(30);
        Label nameLabel = new Label("MokFok");
        nameLabel.setStyle("-fx-text-fill: white");
        Label profDescriptionLabel = new Label("\"owo rawr uwu\"");
        profDescriptionLabel.setStyle("-fx-text-fill: limegreen");
        Button exitButton = new Button("x");
        Button minimizeButton = new Button("-");
        Button settingButton = new Button("⚙");
        exitButton.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white;"+"-fx-font-size: 15");
        minimizeButton.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white;"+"-fx-font-size: 15");
        settingButton.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white;"+"-fx-font-size: 15");
        exitButton.setOnAction(actionEvent -> {
            clientStage.close();
        });

        //social stuff
        Label socialLabel = new Label("Social");
        socialLabel.setStyle("-fx-text-fill: white");
        socialLabel.setPrefSize(100,10);
        Button addFriendButton = new Button("+");
        addFriendButton.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white");
        addFriendButton.setPrefSize(10,10);
        Button removeFriendButton = new Button("-");
        removeFriendButton.setStyle("-fx-background-color: transparent;"+"-fx-text-fill: white");
        addFriendButton.setPrefSize(10,10);

        //Content box
        //menu
        VBox vboxMenuItem1 = new VBox(menuItem1);
        VBox vboxMenuItem2 = new VBox(menuItem2);
        VBox vboxMenuItem3 = new VBox(menuItem3);
        vboxMenuItem1.setStyle(menuVboxIdle);
        vboxMenuItem2.setStyle(menuVboxIdle);
        vboxMenuItem3.setStyle(menuVboxIdle);
        vboxMenuItem1.setOnMouseEntered(e-> vboxMenuItem1.setStyle(menuVboxHover));
        vboxMenuItem1.setOnMouseExited(e-> vboxMenuItem1.setStyle(menuVboxIdle));
        vboxMenuItem2.setOnMouseEntered(e-> vboxMenuItem2.setStyle(menuVboxHover));
        vboxMenuItem2.setOnMouseExited(e-> vboxMenuItem2.setStyle(menuVboxIdle));
        vboxMenuItem3.setOnMouseEntered(e-> vboxMenuItem3.setStyle(menuVboxHover));
        vboxMenuItem3.setOnMouseExited(e-> vboxMenuItem3.setStyle(menuVboxIdle));
        HBox hboxMenuItems = new HBox(vboxMenuItem1, vboxMenuItem2, vboxMenuItem3);
        hboxMenuItems.setPrefSize(600,75);
        Region menuRegionBottom = new Region();
        menuRegionBottom.setStyle("-fx-background-color: white;"+"-fx-opacity: 0.5");
        menuRegionBottom.setPrefSize(600,0.5);
        VBox vboxMenu = new VBox(hboxMenuItems,menuRegionBottom);
        vboxMenu.setStyle("-fx-background-color: rgb(0,0,0,0.5);");
        //home
        VBox vboxHome = new VBox(labelHome, wildrift);
        vboxHome.setAlignment(Pos.CENTER);
        vboxHome.setPrefSize(600, 325);
        /*
        VBox vboxContentHome = new VBox(vboxMenu, vboxHome);
        StackPane contentPaneHome = new StackPane(imageView1, vboxContentHome);
         */
        //play
        VBox vboxPlay = new VBox(gamemode1, labelPlay, enterHellButton);
        vboxPlay.setAlignment(Pos.CENTER);
        vboxPlay.setPrefSize(600, 325);

        //Profile box
        HBox hboxAppButtons = new HBox(settingButton, minimizeButton, exitButton);
        hboxAppButtons.setPrefSize(200, 30);
        hboxAppButtons.setAlignment(Pos.TOP_RIGHT);
        HBox hboxProfileInfo = new HBox(profpic, new VBox(nameLabel, profDescriptionLabel));
        hboxProfileInfo.setPrefSize(200, 45);
        Region profileRegionBottom = new Region();
        profileRegionBottom.setStyle("-fx-background-color: white;"+"-fx-opacity: 0.5");
        profileRegionBottom.setPrefSize(200,0.5);
        VBox vboxProfileInner = new VBox(hboxAppButtons, hboxProfileInfo);
        VBox vboxProfileOuter = new VBox(vboxProfileInner, profileRegionBottom);
        //Social box
        Region regionSocialTitle = new Region();
        TextField socialTF = new TextField("name -> button");
        socialTF.setFont(Font.font("Arial", FontPosture.ITALIC, 10));
        regionSocialTitle.setPrefSize(0,0);
        HBox hboxSocialTitleDefault = new HBox(socialLabel, regionSocialTitle, socialTF, addFriendButton, removeFriendButton);
        hboxSocialTitleDefault.setAlignment(Pos.CENTER_LEFT);
        VBox vboxFriendlist = new VBox();
        vboxFriendlist.setPrefSize(200,200);
        VBox vboxRightPanel = new VBox(vboxProfileOuter, hboxSocialTitleDefault, new HBox(new Label("lol")), vboxFriendlist);
        vboxRightPanel.setStyle("-fx-background-color: #2d2e3b");
        loadFriendslist(vboxFriendlist);
        addFriendButton.setOnAction(actionEvent -> {
            try {
                if (socialTF.getText().contains(" ") || socialTF.getText().isEmpty()){
                    System.out.println("enter a valid name :)");
                    return;
                }
                System.out.println("name is valid");
                addFriendToDatabase(socialTF.getText(), vboxFriendlist);
            } catch (SQLException eg){
                System.out.println("sql error, oh nyooo. We could not add your friend.");
            }
        });
        removeFriendButton.setOnAction(actionEvent -> {
            try {
                if (socialTF.getText().contains(" ") || socialTF.getText().isEmpty()){
                    System.out.println("enter a valid name :)");
                    return;
                }
                System.out.println("name is valid");
                removeFriendFromDatabase(socialTF.getText(), vboxFriendlist);
            } catch (SQLException eg){
                System.out.println("sql error, oh nyooo. We could not remove your friend.");
            }
        });

        //scenes
        HBox hboxMainHome = new HBox(contentPaneHome, vboxRightPanel);
        /*
        HBox hboxMainPlay = new HBox(contentPanePlay, vboxProfileOuter);

         */

        setContent(vboxMenu, vboxHome, imageView1);

        Scene homeScene = new Scene(hboxMainHome,800,400);
        /*
        Scene playScene = new Scene(hboxMainPlay,800,400);

         */

        //scene changers
        menuItem1.setOnAction(actionEvent -> {
            setContent(vboxMenu, vboxPlay, imageView1);
        });
        menuItem2.setOnAction(actionEvent -> {
            setContent(vboxMenu, vboxHome, imageView1);
        });

        clientStage.setScene(homeScene);
        clientStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
