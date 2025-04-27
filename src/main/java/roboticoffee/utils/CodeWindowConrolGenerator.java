package roboticoffee.utils;

import java.util.Optional;

import com.jme3.app.SimpleApplication;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import roboticoffee.states.PeopleState;
import roboticoffee.states.RobotState;
import roboticoffee.states.UIState;

public class CodeWindowConrolGenerator {
    private RobotState robotState;
    private PeopleState peopleState;

    public CodeWindowConrolGenerator(SimpleApplication app, RobotState robotState, PeopleState peopleState) {
        this.peopleState = peopleState;
        this.robotState = robotState;

    }

    public CodeWindowControl generate() {
        Optional<String> name = showTextInputDialog();
        if (name.isPresent()) {
            if (name.get().isEmpty()) {
                showErrorDialog("You must enter a name!");
            } else {
                if (UIState.isCodeWindowNameUnique(name.get())) {
                    CodeWindowControl codeControl = new CodeWindowControl(name.get(), robotState, peopleState);
                    codeControl.setId(name.get());
                    codeControl.setLayoutX(100);
                    codeControl.setLayoutY(100);
                    return codeControl;
                } else {
                    showErrorDialog(name.get() + " is already in use!");
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
