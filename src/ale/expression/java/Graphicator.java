package ale.expression.java;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class Graphicator {

    private final int PARENT_TO_CHILD = 50;
    private final int CHILD_TO_CHILD = 30;
    private final int FONT_SIZE = 18;
    private final int NODE_WIDTH = 35;
    private final int NODE_HEIGHT = 25;
    private final Color NODE_COLOR = Color.SLATEBLUE;
    private final Color FONT_COLOR = Color.WHITE;
    private final Color LINE_COLOR = Color.BLACK;

    private Map<ExpressionTree.Node, Rectangle> nodePositions = new HashMap<>();
    private Map<ExpressionTree.Node, Dimension2D> subtreeSizes = new HashMap<>();

    private Canvas canvas;
    private GraphicsContext graphics;
    private ExpressionTree expressionTree;

    public Graphicator(Canvas canvas) {
        this.canvas = canvas;
        this.graphics = canvas.getGraphicsContext2D();

        prepareCanvas();
    }

    private void prepareCanvas() {
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        graphics.setFill(Color.GRAY);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.BLACK);
        graphics.strokeRect(0, 0, canvas.getWidth() - 0.5, canvas.getHeight());
    }

    public void graphicateExpressionTree(ExpressionTree expressionTree) {
        prepareCanvas();
        this.expressionTree = expressionTree;

        calculatePositions();
        drawExpressionTree(expressionTree.getRoot(), Integer.MAX_VALUE, Integer.MAX_VALUE, 10);
    }

    private void drawExpressionTree(ExpressionTree.Node root, int x, int y, int offset) {
        if (root == null)
            return;

        int XOFFSET = (int) canvas.getWidth()/2;

        Rectangle rect = nodePositions.get(root);

        // Draw node
        graphics.setFill(NODE_COLOR);
        graphics.fillRect(rect.getX() + XOFFSET, rect.getY(), rect.getWidth(), rect.getHeight());

        // Draw value
        graphics.setFill(FONT_COLOR);
        graphics.setFont(new Font(FONT_SIZE));

        if (root.value == -1)
            graphics.fillText(String.valueOf(root.id), rect.getX() + XOFFSET + 7, rect.getY() + offset + 5);
        else
            graphics.fillText(String.valueOf(root.value), rect.getX() + XOFFSET + 7, rect.getY() + offset + 5);

        // Draw line
        graphics.setFill(LINE_COLOR);
        if (x != Integer.MAX_VALUE)
            graphics.strokeLine(x + XOFFSET, y, rect.getX() + rect.getWidth()/2 + XOFFSET, rect.getY());

        drawExpressionTree(root.left, (int) (rect.getX() + rect.getWidth()/2), (int) (rect.getY() + rect.getHeight()), offset);
        drawExpressionTree(root.right, (int) (rect.getX() + rect.getWidth()/2), (int) (rect.getY() + rect.getHeight()), offset);
    }

    private void calculatePositions() {
        nodePositions.clear();
        subtreeSizes.clear();

        ExpressionTree.Node root = expressionTree.getRoot();

        if (root == null)
            return;

        calculateSubtreeSizes(root);
        calculateNodePositions(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
    }

    private Dimension2D calculateSubtreeSizes(ExpressionTree.Node root) {
        if (root == null)
            return new Dimension2D(0, 0);

        Dimension2D leftDimension = calculateSubtreeSizes(root.left);
        Dimension2D rightDimension = calculateSubtreeSizes(root.right);

        int width = (int) leftDimension.getWidth() + CHILD_TO_CHILD + (int) rightDimension.getWidth();
        //int height = fontMetrics.getHeight() + PARENT_TO_CHILD + Math.max(leftDimension.height, rightDimension.height);
        int height = PARENT_TO_CHILD + (int) Math.max(leftDimension.getHeight(), rightDimension.getHeight());

        var dimension = new Dimension2D(width, height);
        subtreeSizes.put(root, dimension);

        return dimension;
    }

    private void calculateNodePositions(ExpressionTree.Node root, int left, int right, int top) {
        if (root == null)
            return;

        Dimension2D leftDimension = subtreeSizes.get(root.left);
        if (leftDimension == null)
            leftDimension = new Dimension2D(0, 0);

        Dimension2D rightDimension = subtreeSizes.get(root.right);
        if (rightDimension == null)
            rightDimension = new Dimension2D(0, 0);

        int center = 0;

        if (left != Integer.MAX_VALUE)
            center = left + (int) leftDimension.getWidth() + CHILD_TO_CHILD/2;

        else if (right != Integer.MAX_VALUE)
            center = right - (int) rightDimension.getWidth() - CHILD_TO_CHILD/2;

        // text width
        int width = 10;

        //nodePositions.put(root, new Rectangle(center - width/2 - 3, top, width + 6, fontMetrics.getHeight()));
        //nodePositions.put(root, new Rectangle(center - width/2 - 3, top, width + 6, 10));
        nodePositions.put(root, new Rectangle(center - width/2 - 3, top, NODE_WIDTH, NODE_HEIGHT));

        //calculateNodePositions(root.left, Integer.MAX_VALUE, center - CHILD_TO_CHILD/2, top + fontMetrics.getHeight() + PARENT_TO_CHILD);
        //calculateNodePositions(root.right, center + CHILD_TO_CHILD/2, Integer.MAX_VALUE, top + fontMetrics.getHeight() + PARENT_TO_CHILD);

        calculateNodePositions(root.left, Integer.MAX_VALUE, center - CHILD_TO_CHILD/2, top + PARENT_TO_CHILD);
        calculateNodePositions(root.right, center + CHILD_TO_CHILD/2, Integer.MAX_VALUE, top + PARENT_TO_CHILD);
    }

}
