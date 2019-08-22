package ale.expression.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionTree {

    public static class Node {
        public String id;
        public double value = -1;

        public Node left;
        public Node right;

        // operator
        public Node(String id) {
            this.id = id;
        }

        // variable
        public Node(String id, double value) {
            this.id = id;
            this.value = value;
        }

        // number
        public Node(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            // operator
            if (value == -1)
                return "(" + id + ")";

            // number
            if (id == null)
                return "(" + value + ")";

            // variable
            return "(" + id + " = " + value + ")";
        }
    }

    private Node root;
    public Node getRoot() { return root; }

    private List<String> tokens;
    private Map<String, Double> variables;

    public ExpressionTree(List<String> tokens, Map<String, Double> variables) {
        this.tokens = tokens;
        this.variables = variables;
    }

    public Node constructTree() {
        root = constructTreeHelper();
        return root;
    }

    private Node constructTreeHelper() {
        Node rootTemp;
        var stack = new Stack<Node>();

        for (String token : tokens) {

            rootTemp = nodeFrom(token);

            if (!isOperator(token))
                stack.push(rootTemp);

            else {
                rootTemp.right = stack.pop();
                rootTemp.left = stack.pop();

                stack.push(rootTemp);
            }
        }

        return stack.pop();
    }

    private Node nodeFrom(String token) {
        if (isOperator(token))
            return new Node(token);

        return isVariable(token)? new Node(token, valueOf(token)) : new Node(Double.parseDouble(token));
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

    public List<Node> traverseInorder() {
        var nodes = new ArrayList<Node>();
        traverseInorder(root, nodes);
        return nodes;
    }

    private void traverseInorder(Node root, List<Node> nodes) {
        if (root != null) {
            traverseInorder(root.left, nodes);
            nodes.add(root);
            traverseInorder(root.right, nodes);
        }
    }

    public List<Node> traversePostorder() {
        var nodes = new ArrayList<Node>();
        traversePostorder(root, nodes);
        return nodes;
    }

    private void traversePostorder(Node root, List<Node> nodes) {
        if (root != null) {
            traversePostorder(root.left, nodes);
            traversePostorder(root.right, nodes);
            nodes.add(root);
        }
    }

    public List<Node> traversePreorder() {
        var nodes = new ArrayList<Node>();
        traversePreorder(root, nodes);
        return nodes;
    }

    private void traversePreorder(Node root, List<Node> nodes) {
        if (root != null) {
            nodes.add(root);
            traversePreorder(root.left, nodes);
            traversePreorder(root.right, nodes);
        }
    }

    private double valueOf(String id) {
        return variables.get(id);
    }

}
