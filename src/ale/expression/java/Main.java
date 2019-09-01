package ale.expression.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
        primaryStage.setTitle("Evaluador de expresiones");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
a) 3 + 6 * 14 = 87
b) 8 + 7 * 3 + 4 * 6 = 53
c) -4 * 7 + 2 ^ 3 / 4 - 5 = -31
d) (33 + 3 * 4) / 5 = 9
e) 2 ^ 2 * 3 = 12
f) 3 + 2 * (18 - 4 ^ 2) = 7
g) 16 * 6 - 3 * 2 = 90
h) 3 + 3 * 2 = 9
i) 9 + 7 * 8 - 36 / 5 = 57.8
j) 7 * 5 * 3 / 4 / 3 = 8.75
k) 3.5 * 2 + 7 - 3 * 2 / 2.0 = 11
l) 4 + 36 ^ (1.0 / 2) = 28 CHECAR (10)
m) 4 * 4 + 2 * 5 / 2.0 = 21
n) 3 * 7.0 / 2 - 3 ^ (2 - 2) = 9.5
o) 2 ^ (3 / 2) + 4 = 6.8284
p) 5 ^ 1.0 / 4 - 2 = -0.75

A = 4; B = 5; C = 1
a) B * A - B ^ 2 / 4 * C = 13.75
b) (A * B) / 3 ^ 2 = 2.2222
c) (((B + C) / 2 * A + 10) * 3 * B) - 6 = 324
d) A ^ B ^ C = 1024
 */
