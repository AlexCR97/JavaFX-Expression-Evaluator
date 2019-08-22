package ale.expression.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public class Variable {
        private String id;
        private String value;

        public Variable() {}

        public Variable(String id, String value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @FXML
    private TextField tfExpresion;

    @FXML
    private Text txResultado;

    @FXML
    private Button btCalcular;

    @FXML
    private Button btEliminar;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField tfVariable;

    @FXML
    private TextField tfValor;

    @FXML
    private Button btAgregar;

    @FXML
    private TableView<Variable> tvValores;

    @FXML
    private TableColumn<Variable, String> tcVariable;

    @FXML
    private TableColumn<Variable, String> tcValor;

    private GraphicsContext graphics;
    private ObservableList<Variable> variables = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Prepare canvas
        graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.GRAY);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.BLACK);
        graphics.strokeRect(0, 0, canvas.getWidth() - 0.5, canvas.getHeight());

        // Prepare table
        tcVariable.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcValor.setCellValueFactory(new PropertyValueFactory<>("value"));
        tvValores.setItems(variables);

        // Set listeners
        btCalcular.setOnAction(btCalcularOnAction);
        btEliminar.setOnAction(btEliminarOnAction);
        btAgregar.setOnAction(btAgregarOnAction);
    }

    private final EventHandler<ActionEvent> btCalcularOnAction = event -> {
        String infixExpression = tfExpresion.getText();

        if (infixExpression.isEmpty()) {
            var alert = new Alert(AlertType.ERROR);
            alert.setTitle("Evaluador de expresiones");
            alert.setHeaderText("Error");
            alert.setContentText("Debes ingresar una expresion aritmética");

            alert.showAndWait().ifPresent((result) -> {
                if (result == ButtonType.OK)
                    System.out.println("Pressed OK");

                else if (result == ButtonType.CLOSE)
                    System.out.println("Pressed CLOSE");
            });

            return;
        }

        // Get tokens
        List<String> infixTokens = ExpressionConverter.tokensFrom(infixExpression);
        List<String> postfixTokens = ExpressionConverter.infixToPostfix(infixTokens);

        // Get variables
        var values = new HashMap<String, Double>();
        variables.forEach((variable) -> {
            values.put(variable.id, Double.parseDouble(variable.value));
        });

        // Evaluate
        var evaluator = new ExpressionEvaluator(postfixTokens, values);
        double result = evaluator.evaluate();

        // Construct tree
        var tree = new ExpressionTree(postfixTokens, values);
        tree.constructTree();

        // Update view
        txResultado.setText(infixExpression + " = " + result);
    };

    private final EventHandler<ActionEvent> btEliminarOnAction = event -> {
        int selectedVariable = tvValores.getSelectionModel().getSelectedIndex();

        if (selectedVariable <= -1)
            return;

        variables.remove(selectedVariable);
    };

    private final EventHandler<ActionEvent> btAgregarOnAction = event -> {
        String id = tfVariable.getText();
        String value = tfValor.getText();

        if (id.isEmpty() || value.isEmpty())
            return;

        try {
            Double.parseDouble(value);
        }
        catch (NumberFormatException ex) {
            var alert = new Alert(AlertType.ERROR);
            alert.setTitle("Evaluador de expresiones");
            alert.setHeaderText("Error");
            alert.setContentText("El valor numerico ingresado es invalido: '" + value + "'");

            alert.showAndWait().ifPresent((result) -> {
                if (result == ButtonType.OK)
                    System.out.println("Pressed OK");

                else if (result == ButtonType.CLOSE)
                    System.out.println("Pressed CLOSE");
            });

            return;
        }
        finally {
            tfVariable.clear();
            tfValor.clear();
        }

        var variable = new Variable(id, value);
        variables.add(variable);
    };

}

