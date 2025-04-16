package roboticoffee.utils;

import java.util.Optional;

import com.jme3.app.SimpleApplication;

import io.tlf.jme.jfx.JavaFxUI;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import roboticoffee.states.UIState;

public class CodeWindowConrolGenerator {

    public CodeWindowConrolGenerator(SimpleApplication app) {

    }

    public CodeWindowControl generate() {
        Optional<String> name = showTextInputDialog();
        if (name.isPresent()) {
            if (name.get().isEmpty()) {
                showErrorDialog("You must enter a name!");
            } else {
                if (UIState.isCodeWindowNameUnique(name.get())) {
                    CodeWindowControl codeControl = new CodeWindowControl(name.get());
                    codeControl.setLayoutX(100);
                    codeControl.setLayoutY(100);
                    return codeControl;
                }
                else {
                    showErrorDialog( name.get() + " is already in use!");
                }
            }
        }
        return null;
    }

    private Optional<String> showTextInputDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generate New Code Window");
        dialog.setHeaderText("Enter the name of the new code window:");

        return dialog.showAndWait();

    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
