package sample;

import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;

public class diabloGame {
    private int gameTimeSeconds = 0;
    private int enemySpawnInterval = 5;
    private Box boxTerrain1 = new Box(50,50,50);
    private Box boxTerrain2 = new Box(50,50,50);
    private Box playerBox1 = new Box(50,50,100);
    private Box playerBox2 = new Box(25,25,150);
    private Box playerBox3 = new Box(25,25,150);
    private Box enemy1Box1 = new Box(100,100,100);
    private Box enemy1Box2 = new Box(75,25,25);
    private Box enemy1Box3 = new Box(75,25,25);
    Group grpCurrentEnemies = new Group();
    ArrayList<Group> enemiesListLeft = new ArrayList<>();
    ArrayList<Group> enemiesListMiddle = new ArrayList<>();
    ArrayList<Group> enemiesListRight = new ArrayList<>();
    private String playerPositionStatus = "middle";
    private Label playerPositionStatusLabel = new Label(playerPositionStatus);
    private int score = 0;
    private Label scoreLabel = new Label("Score: "+score);
    private Label playerSmallZLabel = new Label("Small Z: "+playerBox1.getTranslateZ());
    private Label playerBigZLabel = new Label("Big Z: "+(playerBox1.getTranslateZ()+playerBox1.getDepth()));
    private int playerLife = 3;
    private Label playerLifeLabel = new Label();
    private Box centerBox = new Box(5,100,5);
    private Box groundPlane = new Box(500,1,5000);
    private PhongMaterial matSandybrown = new PhongMaterial(Color.SANDYBROWN);
    private PhongMaterial matRed = new PhongMaterial(Color.RED);
    private PhongMaterial matGreen = new PhongMaterial(Color.GREEN);
    private PhongMaterial matDarkGray = new PhongMaterial(Color.DARKGRAY);
    private PerspectiveCamera mainCamera = new PerspectiveCamera();
    private ArrayList<Box> playerBoxList = new ArrayList<>();

    private void updateGame(){
        gameTimeSeconds++;
        System.out.println("Time: "+gameTimeSeconds);
        if (gameTimeSeconds%10==0){
            score++;
            scoreLabel.setText("Score: "+score);
        }
        if (gameTimeSeconds%enemySpawnInterval==0){
            spawnEnemy1();
        }
        checkForCollision();
    }

    private void updatePlayerLifeLabel(){
        playerLifeLabel.setText("");
        String life = "";
        for (int i = 0; i < playerLife; i++) {
            life+="<3";
        }
        playerLifeLabel.setText(life);
    }

    private void checkForCollision(){
        //this method should be check when a second has passed and when player moves.
        //run the arraylist of enemies through and check is position is within player
        double playerSmallZ = Math.round(grpPlayer.getTranslateZ()+500);
        double playerBigZ = Math.round(grpPlayer.getTranslateZ()+500+100);
        playerSmallZLabel.setText("Small Z: "+playerSmallZ);
        playerBigZLabel.setText("Big Z: "+playerBigZ);
        boolean collided = false;
        ArrayList<Group> enemyList = null;
        switch (playerPositionStatusLabel.getText()){
            case "left": enemyList=enemiesListLeft; break;
            case "middle": enemyList=enemiesListMiddle; break;
            case "right": enemyList=enemiesListRight; break;
        }
        for (Group enemy : enemyList){
            double distBetweenMinZ = enemy.getTranslateZ()-500-grpPlayer.getTranslateZ();
            //System.out.println("distance to enemy: "+distBetweenMinZ);
            if (distBetweenMinZ<200 && distBetweenMinZ>-200){
                collided=true;
            }
        }
        if (collided){
            gameOver();
        }
    }
     //TODO
    public void gameOver(){
        System.out.println("Collision was detected and its game over buddy XD");
        //call methpd in main to swap sceness
        Main main = new Main();
        main.setSceneToGameOver(score);
    }

    public void spawnEnemy1(){
        //every 100 seconds delete enemies in the game.
        if (gameTimeSeconds%50==0){
            grpCurrentEnemies.getChildren().clear();
            enemiesListLeft.clear();
            enemiesListMiddle.clear();
            enemiesListRight.clear();
            System.out.println("All enemies are deleted.");
        }
        int n = (int)(Math.random()*3);
        System.out.print("lane we want to spawn: "+n+" ");
        switch (n){
            case 0: spawnEnemy2(enemiesListLeft,0); break;
            case 1: spawnEnemy2(enemiesListMiddle,1); break;
            case 2: spawnEnemy2(enemiesListRight,2); break;
        }
    }

    public void spawnEnemy2(ArrayList laneList, int lane){
        //create a new enemy
        Group enemyToSpawn = new Group();
        int n = (int)(Math.random()*3);
        switch (n){
            case 0: enemyToSpawn=getEnemyGroup1(); System.out.println("we got enemy1"); break;
            case 1: enemyToSpawn=getEnemyGroup2(); System.out.println("we got enemy2"); break;
            case 2: enemyToSpawn=getEnemyGroup3(); System.out.println("we got enemy3"); break;
        }
        laneList.add(enemyToSpawn);
        grpCurrentEnemies.getChildren().add(enemyToSpawn);
        switch (lane){
            case 0: enemyToSpawn.setTranslateZ(cameraGroup.getTranslateZ()+1500); enemyToSpawn.setTranslateX(-400); break;
            case 1: enemyToSpawn.setTranslateZ(cameraGroup.getTranslateZ()+1500); enemyToSpawn.setTranslateX(0); break;
            case 2: enemyToSpawn.setTranslateZ(cameraGroup.getTranslateZ()+1500); enemyToSpawn.setTranslateX(400); break;
        }
    }

    private Group getEnemyGroup1(){
        Group enemy = new Group();
        Box boxBody = new Box(50,50,50);
        boxBody.setMaterial(matRed);
        enemy.getChildren().addAll(boxBody);
        return enemy;
    }
    private Group getEnemyGroup2(){
        Group enemy = new Group();
        Box boxBody = new Box(50,50,50);
        boxBody.setMaterial(matGreen);
        enemy.getChildren().addAll(boxBody);
        return enemy;
    }
    private Group getEnemyGroup3(){
        Group enemy = new Group();
        Box boxBody = new Box(50,50,50);
        boxBody.setMaterial(matDarkGray);
        enemy.getChildren().addAll(boxBody);
        return enemy;
    }

    public void moveCameraW(){
        mainCamera.translateZProperty().set(mainCamera.translateZProperty().get()+10);
    }

    public void moveCameraS(){
        mainCamera.translateZProperty().set(mainCamera.translateZProperty().get()-10);
    }

    public void movePlayerLeft(){
        if (!playerPositionStatus.equals("left")) {
            if (playerPositionStatus.equals("middle"))
                playerPositionStatus = "left";
            else playerPositionStatus = "middle";
            playerPositionStatusLabel.setText(playerPositionStatus);
            for (Box box : playerBoxList) {
                box.translateXProperty().set(box.translateXProperty().get() - 400);
                checkForCollision();
            }
        }
    }

    public void movePlayerRight(){
        if (!playerPositionStatus.equals("right")) {
            if (playerPositionStatus.equals("middle"))
                playerPositionStatus="right";
            else playerPositionStatus="middle";
            playerPositionStatusLabel.setText(playerPositionStatus);
            for (Box box : playerBoxList) {
                box.translateXProperty().set(box.translateXProperty().get() + 400);
                checkForCollision();
            }
        }
    }

    public void moveCameraUp(){
        mainCamera.translateYProperty().set(mainCamera.translateYProperty().get()-10);
    }

    public void moveCameraDown(){
        mainCamera.translateYProperty().set(mainCamera.translateYProperty().get()+10);
    }

    public void flyForwardCamera() {
        flyForwardPlayer();
        Timeline forwardAnimation = new Timeline();
        KeyValue destinationZ = new KeyValue(getCameraGroup().translateZProperty(),getZDestination());
        KeyFrame kf = new KeyFrame(Duration.seconds(1),destinationZ);
        forwardAnimation.getKeyFrames().add(kf);
        forwardAnimation.play();
        forwardAnimation.setOnFinished(event -> {
            getCameraGroup().translateZProperty().set(getZDestination());
            setZDestination(getZDestination()+100);
            //System.out.println("new camera starting position: "+mainCamera.translateZProperty().get()+" and new zDestination: "+getZDestination());
            //remove previous keyValue/keyFrame
            forwardAnimation.getKeyFrames().remove(0);
            //create new keyValue/keyFrame
            forwardAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(getCameraGroup().translateZProperty(),getZDestination())));
            updateGame();
            forwardAnimation.play();
        });
    }
    private int zDestination = 100;
    private void setZDestination(int n){ zDestination=n; }
    private int getZDestination(){ return zDestination; }

    public void flyForwardPlayer() {
        Timeline forwardAnimation = new Timeline();
        KeyValue destinationZ2 = new KeyValue(grpPlayer.translateZProperty(),getZDestination2());
        KeyFrame kf = new KeyFrame(Duration.seconds(1),destinationZ2);
        forwardAnimation.getKeyFrames().add(kf);
        forwardAnimation.play();
        forwardAnimation.setOnFinished(event -> {
            grpPlayer.translateZProperty().set(getZDestination2());
            setZDestination2(getZDestination2()+100);
            //System.out.println("new camera starting position: "+mainCamera.translateZProperty().get()+" and new zDestination: "+getZDestination());
            //remove previous keyValue/keyFrame
            forwardAnimation.getKeyFrames().remove(0);
            //create new keyValue/keyFrame
            forwardAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(grpPlayer.translateZProperty(),getZDestination2())));

            forwardAnimation.play();
        });
    }
    private int zDestination2 = 100;
    private void setZDestination2(int n){ zDestination2=n; }
    private int getZDestination2(){ return zDestination2; }

    private Group cameraGroup;
    private Group getCameraGroup(){ return cameraGroup; }
    private void setCameraGroup(Group grp){ cameraGroup=grp; }

    private void assemblePlayer(){
        playerBox1.setTranslateZ(500);
        playerBox2.setTranslateZ(500+(playerBox2.getDepth()-playerBox1.getDepth()));
        playerBox2.setTranslateX(-(playerBox1.widthProperty().get()/2));
        playerBox3.setTranslateZ(500+(playerBox3.getDepth()-playerBox1.getDepth()));
        playerBox3.setTranslateX((playerBox1.widthProperty().get()/2));
    }

    public Group grpPlayer;

    public Scene getMainGroup() {
        updatePlayerLifeLabel();
        assemblePlayer();
        boxTerrain1.setMaterial(matSandybrown);
        boxTerrain2.setMaterial(matSandybrown);
        playerBox2.setMaterial(matDarkGray);
        playerBox3.setMaterial(matDarkGray);
        enemy1Box1.setMaterial(matRed);
        enemy1Box2.setMaterial(matDarkGray);
        enemy1Box3.setMaterial(matDarkGray);
        groundPlane.setTranslateY(25);
        playerBoxList.add(playerBox1);
        playerBoxList.add(playerBox2);
        playerBoxList.add(playerBox3);
        boxTerrain1.setTranslateY(-25);
        boxTerrain1.setTranslateX(150);
        boxTerrain1.setTranslateZ(150);
        boxTerrain2.setTranslateY(-25);
        boxTerrain2.setTranslateX(-150);
        boxTerrain2.setTranslateZ(-150);
        playerSmallZLabel.setTranslateX(150);
        playerBigZLabel.setTranslateX(250);
        playerSmallZLabel.setStyle("-fx-text-fill: white");
        playerBigZLabel.setStyle("-fx-text-fill: white");
        groundPlane.setMaterial(matSandybrown);
        Group playerGroup = new Group();
        playerGroup.getChildren().addAll(playerBox3, playerBox2, playerBox1);
        grpPlayer=playerGroup;
        playerPositionStatusLabel.setStyle("-fx-text-fill: white;");
        scoreLabel.setStyle("-fx-text-fill: white");
        scoreLabel.setTranslateX(50);
        playerLifeLabel.setStyle("-fx-text-fill: white");
        playerLifeLabel.setTranslateX(400-20);
        Group grpBox = new Group();
        grpBox.getChildren().addAll(boxTerrain1, boxTerrain2, centerBox);
        Group grpPlane = new Group(groundPlane);
        Group grpCamera = new Group(mainCamera, playerPositionStatusLabel, scoreLabel, playerLifeLabel, playerSmallZLabel, playerBigZLabel);
        grpCamera.translateYProperty().set(-400);
        grpCamera.translateXProperty().set(-400);
        grpCamera.translateZProperty().set(0);
        grpCamera.setRotationAxis(Rotate.X_AXIS);
        grpCamera.rotateProperty().set(-10);
        setCameraGroup(grpCamera);
        Group grpMain = new Group();
        grpMain.getChildren().addAll(grpPlane, grpBox, playerGroup, grpCamera, grpCurrentEnemies);
        //true if z buffer
        Scene scene = new Scene(grpMain, 800,400,true);
        scene.setFill(Color.BLACK);
        scene.setCamera(mainCamera);
        return scene;
    }
}
