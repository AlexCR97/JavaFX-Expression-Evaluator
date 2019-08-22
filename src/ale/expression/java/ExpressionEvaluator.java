package ale.expression.java;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionEvaluator {

    private List<String> tokens;
    private Map<String, Double> variables;

    public ExpressionEvaluator(List<String> tokens, Map<String, Double> variables) {
        this.tokens = tokens;
        this.variables = variables;
    }

    public double evaluate() {
        var stack = new Stack<String>();

        tokens.forEach((token) -> {

            if (!isOperator(token)) {
                stack.push(token);
            }
            else {
                String a = stack.pop();
                String b = stack.pop();
                String c = performOperation(b, a, token);

                stack.push(c);
            }

        });

        return Double.parseDouble(stack.pop());
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private boolean isVariable(String token) {
        try {
            Double.parseDouble(token);
            return false;
        } catch (NumberFormatException ex) {
            return true;
        }
    }

    private String performOperation(String a, String b, String operator) {
        double aValue;
        if (isVariable(a))
            aValue = variables.get(a);
        else
            aValue = Double.parseDouble(a);

        double bValue;
        if (isVariable(b))
            bValue = variables.get(b);
        else
            bValue = Double.parseDouble(b);

        switch (operator) {
            case "+": return String.valueOf(aValue + bValue);
            case "-": return String.valueOf(aValue - bValue);
            case "*": return String.valueOf(aValue * bValue);
            case "/": return String.valueOf(aValue / bValue);
            default: return null;
        }
    }

}
