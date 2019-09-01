package ale.expression.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private Graphicator graphicator;
    private ObservableList<Variable> variables = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphicator = new Graphicator(canvas);

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
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Error",
                    "Debes ingresar una expresión aritmética",
                    AlertType.ERROR
            );
            return;
        }

        // Get tokens
        //List<String> infixTokens = ExpressionConverter.tokensFrom(infixExpression);
        List<String> infixTokens = ExpressionConverter.tokensFrom(infixExpression);

        if (infixTokens == null) {
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Error",
                    "La expresion ingresada es invalida",
                    AlertType.ERROR
            );
            return;
        }

        List<String> postfixTokens = ExpressionConverter.infixToPostfix(infixTokens);

        System.out.println("Infix tokens:   " + infixTokens);
        System.out.println("Postfix tokens: " + postfixTokens);

        // Get variables
        var values = new HashMap<String, Double>();
        variables.forEach((variable) -> {
            values.put(variable.id, Double.parseDouble(variable.value));
        });

        // Evaluate
        var evaluator = new ExpressionEvaluator(postfixTokens, values);
        double result;

        try {
            result = evaluator.evaluate();
        } catch (Exception ex) {
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Error",
                    "Revisa que ingresado correctamente la expresion",
                    AlertType.ERROR
            );
            return;
        }

        // Construct tree
        var tree = new ExpressionTree(postfixTokens, values);

        try {
            tree.constructTree();
        } catch (Exception ex) {
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Error",
                    "Revisa que ingresado correctamente la expresion",
                    AlertType.ERROR
            );
            return;
        }

        // Update view
        txResultado.setText(infixExpression + " = " + result);
        graphicator.graphicateExpressionTree(tree);
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

        if (id.isEmpty() || value.isEmpty()) {
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Consejo",
                    "Intenta ingresar un identificador y un valor para la variable",
                    AlertType.INFORMATION
            );
            return;
        }

        if (!isIdentifier(id) && !isNumber(value)) {
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Error",
                    "Uno de los valores ingresados no es valido",
                    AlertType.ERROR
            );
            tfVariable.clear();
            tfValor.clear();
            return;
        }

        if (identifierAlreadyDeclared(id)) {
            showErrorMessage(
                    "Evaluador de expresiones",
                    "Error",
                    "Ya existe una variable declarada con el identificador '" + id + "'",
                    AlertType.ERROR
            );
            tfVariable.clear();
            tfValor.clear();
            return;
        }

        var variable = new Variable(id, value);
        variables.add(variable);

        tfVariable.clear();
        tfValor.clear();
    };

    private boolean identifierAlreadyDeclared(String id) {
        return variables.stream().anyMatch(variable -> variable.id.equals(id));
    }

    private boolean isIdentifier(String input) {
        try {
            Double.parseDouble(input);
            return false;
        } catch (NumberFormatException ex) {
            return true;
        }
    }

    private boolean isNumber(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void showErrorMessage(String title, String header, String message, AlertType alertType) {
        var alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait().ifPresent((result) -> {
            if (result == ButtonType.OK)
                System.out.println("Pressed OK");

            else if (result == ButtonType.CLOSE)
                System.out.println("Pressed CLOSE");
        });
    }
}

