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
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
((x + 8) / 2)^2; x = 12
4x^2 + 5x - 1; x = 3
(n - 7) / (m + 5); n = 4; m = -2
((3 + 2)^2 - 15) / 2 * 5
((3 + 2)^2 - 15) / (2 * 5)
2n + 5; n = 12
4x^2 - 10x; x = 3
3x^2 + 2x - 8; x = -2

(x + y * ( x + 1 * y / (y * x) )) + (c * (m / n))

10 + 20 - 30 * 40 / 50
2x + 10
mx + b
ax^2
c - y / 2
mc^2
 */
