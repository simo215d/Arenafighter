package sample;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class diabloGame {
    private Box box1 = new Box(50,50,50);
    private Box groundPlane = new Box(1000,1,1000);
    private PhongMaterial matGold = new PhongMaterial(Color.GOLD);
    private PhongMaterial matBlue = new PhongMaterial(Color.BLUE);
    private PerspectiveCamera mainCamera = new PerspectiveCamera();

    public void moveWSphere(){
        mainCamera.setRotationAxis(Rotate.X_AXIS);
        mainCamera.rotateProperty().set(mainCamera.getRotate()-1);
    }

    public void moveSSphere(){
        mainCamera.setRotationAxis(Rotate.X_AXIS);
        mainCamera.rotateProperty().set(mainCamera.getRotate()+1);
    }

    public void rotateDSphere(){
        mainCamera.setRotationAxis(Rotate.Y_AXIS);
        mainCamera.rotateProperty().set(mainCamera.getRotate()+1);
    }

    public void rotateASphere(){
        mainCamera.setRotationAxis(Rotate.Y_AXIS);
        mainCamera.rotateProperty().set(mainCamera.getRotate()-1);
    }

    public Scene getScene(){
        box1.setMaterial(matGold);
        box1.translateYProperty().set(-box1.getHeight()/2);
        groundPlane.setMaterial(matBlue);
        Group grpBox = new Group();
        grpBox.getChildren().add(box1);
        Group grpPlane = new Group(groundPlane);
        Group grpMain = new Group();
        grpMain.getChildren().addAll(grpBox, grpPlane);
        mainCamera.translateYProperty().set(-300);
        mainCamera.translateXProperty().set(-400);
        mainCamera.translateZProperty().set(-200);
        Scene scene = new Scene(grpMain, 800,400);
        scene.setFill(Color.BLACK);
        scene.setCamera(mainCamera);
        return scene;
    }
}
