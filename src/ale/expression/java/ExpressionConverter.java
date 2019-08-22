package ale.expression.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ExpressionConverter {

    public static List<String> tokensFrom(String expression) {
        String[] tokens = expression.split(" ");
        return new ArrayList<>(Arrays.asList(tokens));
    }

    public static List<String> infixToPostfix(List<String> infixTokens) {

        var postfixTokens = new ArrayList<String>();
        var stack = new Stack<String>();
        String popped;

        for (String token : infixTokens) {
            String currentToken = String.valueOf(token);

            if (!isOperator(currentToken))
                postfixTokens.add(currentToken);

            else if (currentToken.equals(")"))
                while (!(popped = stack.pop()).equals("("))
                    postfixTokens.add(popped);

            else {
                while (!stack.isEmpty() && !currentToken.equals("(") && precedenceOf(stack.peek()) >= precedenceOf(currentToken))
                    postfixTokens.add(stack.pop());

                stack.push(currentToken);
            }
        }

        while (!stack.isEmpty())
            postfixTokens.add(stack.pop());

        return postfixTokens;
    }

    private static boolean isOperator(String token) {
        return precedenceOf(token) > 0;
    }

    private static int precedenceOf(String token) {
        if (token.equals("(") || token.equals(")"))
            return 1;

        if (token.equals("+") || token.equals("-"))
            return 2;

        if (token.equals("*") || token.equals("/"))
            return 3;

        return -1;
    }

}
