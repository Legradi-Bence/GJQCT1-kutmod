package roboticoffee.utils;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ConsoleControl extends StackPane {
    private TextArea ordersLabel;
    private TextArea consoleLabel;

    public ConsoleControl() {

        ordersLabel = new TextArea("Orders:");
        ordersLabel.setEditable(false);
        ordersLabel.setWrapText(true);
        ordersLabel.setStyle("-fx-control-inner-background: black; -fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

        consoleLabel = new TextArea();
        consoleLabel.setEditable(false);
        consoleLabel.setWrapText(false);
        consoleLabel.setStyle("-fx-control-inner-background: black; -fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

        VBox content = new VBox(10);
        content.getChildren().addAll(ordersLabel, consoleLabel);

        this.getChildren().add(content);

        this.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-control-inner-background: black; -fx-background-color: white;");
        this.setPrefSize(400, 400);
    }

    public void setOrders(String orders) {
        ordersLabel.setText("Orders:\n" + orders);
    }

    public void appendToConsole(String message) {
        consoleLabel.setText(consoleLabel.getText() + message + "\n");
    }
}
