package sample;

import javafx.scene.Scene;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class GameOver {
    int score = 0;

    public void setScore(int x){
        score=x;
    }

    public Scene getScene(){
        VBox xd = new VBox(new TextField("FUCK YOU"));
        Group lol = new Group(xd);
        Scene scene = new Scene(lol, 800,400);
        return scene;
    }
}
