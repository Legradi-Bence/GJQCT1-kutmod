package roboticoffee.states;

import java.util.ArrayList;
import java.util.List;

import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.KeyListener;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.style.BaseStyles;

import roboticoffee.utils.TextArea;

public class UIState extends AbstractAppState {

    private final Node rootNode;
    private final Node guiNode;
    private TextArea textField;

    public UIState(SimpleApplication app) {
        rootNode = app.getRootNode();
        guiNode = app.getGuiNode();
    }

    public String getText() {
        return textField.getText();
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        Screen screen = new Screen(app);
        
        textField = new TextArea(screen,new Vector2f(20, 20), new Vector2f(300, 500));
        screen.addElement(textField.getBasePanel());
        guiNode.addControl(screen);


/* 
        GuiGlobals.initialize(app);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        GuiGlobals.getInstance().getStyles().getSelector("label", "glass").set("background", new QuadBackgroundComponent(new ColorRGBA(0, 0, 0, 1)));
        GuiGlobals.getInstance().getStyles().getSelector("label", "glass").set("color", ColorRGBA.White);

        Container window = new Container();
        window.setLocalTranslation(20, 700, 0);
        label = new Label("");
        label.setPreferredSize(new Vector3f(300, 500, 0));
        window.addChild(label);*/


/*
        InputManager inputManager = app.getInputManager();
        inputManager.addMapping("key_shift", new KeyTrigger(KeyInput.KEY_LSHIFT), new KeyTrigger(KeyInput.KEY_RSHIFT));
        inputManager.addMapping("key_ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL), new KeyTrigger(KeyInput.KEY_RCONTROL));
        inputManager.addMapping("key_alt", new KeyTrigger(KeyInput.KEY_LMENU), new KeyTrigger(KeyInput.KEY_RMENU));
        inputManager.addMapping("key_A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("key_B", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("key_C", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("key_D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("key_E", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("key_F", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("key_G", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("key_H", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("key_I", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("key_J", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("key_K", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("key_L", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("key_M", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("key_N", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addMapping("key_O", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("key_P", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("key_Q", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("key_R", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("key_S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("key_T", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("key_U", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("key_V", new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("key_W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("key_X", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("key_Y", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("key_Z", new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addMapping("key_1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("key_2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("key_3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("key_4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("key_5", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("key_6", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("key_7", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("key_8", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("key_9", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("key_0", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("key_space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("key_enter", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("key_backspace", new KeyTrigger(KeyInput.KEY_BACK));
        inputManager.addMapping("key_slash", new KeyTrigger(KeyInput.KEY_SLASH));
        inputManager.addMapping("key_period", new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addMapping("key_comma", new KeyTrigger(KeyInput.KEY_COMMA));
        inputManager.addMapping("key_minus", new KeyTrigger(KeyInput.KEY_SUBTRACT));
        inputManager.addMapping("key_plus", new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("key_divide", new KeyTrigger(KeyInput.KEY_DIVIDE));
        inputManager.addMapping("key_multiply", new KeyTrigger(KeyInput.KEY_MULTIPLY));
        inputManager.addMapping("key_decimal", new KeyTrigger(KeyInput.KEY_DECIMAL));
        inputManager.addMapping("key_num_0", new KeyTrigger(KeyInput.KEY_NUMPAD0));
        inputManager.addMapping("key_num_1", new KeyTrigger(KeyInput.KEY_NUMPAD1));
        inputManager.addMapping("key_num_2", new KeyTrigger(KeyInput.KEY_NUMPAD2));
        inputManager.addMapping("key_num_3", new KeyTrigger(KeyInput.KEY_NUMPAD3));
        inputManager.addMapping("key_num_4", new KeyTrigger(KeyInput.KEY_NUMPAD4));
        inputManager.addMapping("key_num_5", new KeyTrigger(KeyInput.KEY_NUMPAD5));
        inputManager.addMapping("key_num_6", new KeyTrigger(KeyInput.KEY_NUMPAD6));
        inputManager.addMapping("key_num_7", new KeyTrigger(KeyInput.KEY_NUMPAD7));
        inputManager.addMapping("key_num_8", new KeyTrigger(KeyInput.KEY_NUMPAD8));
        inputManager.addMapping("key_num_9", new KeyTrigger(KeyInput.KEY_NUMPAD9));
        inputManager.addMapping("key_num_enter", new KeyTrigger(KeyInput.KEY_NUMPADENTER));
        inputManager.addMapping("key_tab", new KeyTrigger(KeyInput.KEY_TAB));
        inputManager.addListener(actionListener, "key_shift", "key_ctrl", "key_alt", "key_A", "key_B", "key_C", "key_D", "key_E", "key_F", "key_G", "key_H", "key_I", "key_J",
                "key_K", "key_L", "key_M", "key_N", "key_O", "key_P", "key_Q", "key_R", "key_S", "key_T", "key_U", "key_V", "key_W", "key_X", "key_Y", "key_Z", "key_space",
                "key_enter", "key_backspace", "key_1", "key_2", "key_3", "key_4", "key_5", "key_6", "key_7", "key_8", "key_9", "key_0", "key_slash", "key_period", "key_comma",
                "key_minus", "key_plus", "key_divide", "key_multiply", "key_decimal", "key_num_0", "key_num_1", "key_num_2", "key_num_3", "key_num_4", "key_num_5", "key_num_6",
                "key_num_7", "key_num_8", "key_num_9", "key_num_enter", "key_tab");
*/
        //guiNode.attachChild(window);
    }
/* 
    private boolean isShiftPressed = false;
    private boolean isControlPressed = false;
    private boolean isAltPressed = false;
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if ("key_shift".equals(name)) {
                isShiftPressed = isPressed;
                return;
            }
            if ("key_ctrl".equals(name)) {
                isControlPressed = isPressed;
                return;
            }
            if ("key_alt".equals(name)) {
                isAltPressed = isPressed;
                return;
            }
            if (!isPressed) {
                return;
            }

            switch (name) {
                case "key_A":
                    label.setText(label.getText().concat(isShiftPressed ? "A" : "a"));
                    break;
                case "key_B":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("{"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "B" : "b"));
                    }
                    break;
                case "key_C":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("&"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "C" : "c"));
                    }
                    break;
                case "key_D":
                    label.setText(label.getText().concat(isShiftPressed ? "D" : "d"));
                    break;
                case "key_E":
                    label.setText(label.getText().concat(isShiftPressed ? "E" : "e"));
                    break;
                case "key_F":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("["));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "F" : "f"));
                    }
                    break;
                case "key_G":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("]"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "G" : "g"));
                    }
                    break;
                case "key_H":
                    label.setText(label.getText().concat(isShiftPressed ? "H" : "h"));
                    break;
                case "key_I":
                    label.setText(label.getText().concat(isShiftPressed ? "I" : "i"));
                    break;
                case "key_J":
                    label.setText(label.getText().concat(isShiftPressed ? "J" : "j"));
                    break;
                case "key_K":
                    label.setText(label.getText().concat(isShiftPressed ? "K" : "k"));
                    break;
                case "key_L":
                    label.setText(label.getText().concat(isShiftPressed ? "L" : "l"));
                    break;
                case "key_M":

                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("<"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "M" : "m"));
                    }
                    break;
                case "key_N":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("}"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "N" : "n"));
                    }
                    break;
                case "key_O":
                    label.setText(label.getText().concat(isShiftPressed ? "O" : "o"));
                    break;
                case "key_P":
                    label.setText(label.getText().concat(isShiftPressed ? "P" : "p"));
                    break;
                case "key_Q":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("\\"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "Q" : "q"));
                    }
                    break;
                case "key_R":
                    label.setText(label.getText().concat(isShiftPressed ? "R" : "r"));
                    break;
                case "key_S":
                    label.setText(label.getText().concat(isShiftPressed ? "S" : "s"));
                    break;
                case "key_T":
                    label.setText(label.getText().concat(isShiftPressed ? "T" : "t"));
                    break;
                case "key_U":
                    label.setText(label.getText().concat(isShiftPressed ? "U" : "u"));
                    break;
                case "key_V":

                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("@"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "V" : "v"));
                    }
                    break;
                case "key_W":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("|"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "W" : "w"));
                    }
                    break;
                case "key_X":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("#"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "X" : "x"));
                    }
                    break;
                case "key_Y":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat(">"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "Y" : "y"));
                    }
                    break;
                case "key_Z":
                    label.setText(label.getText().concat(isShiftPressed ? "Z" : "z"));
                    break;
                case "key_space":
                    label.setText(label.getText().concat(" "));
                    break;
                case "key_enter":
                    label.setText(label.getText().concat("\n"));
                    break;
                case "key_1":
                    label.setText(label.getText().concat(isShiftPressed ? "'" : "1"));
                    break;
                case "key_2":
                    label.setText(label.getText().concat(isShiftPressed ? "\"" : "2"));
                    break;
                case "key_3":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat("^"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "+" : "3"));
                    }
                    break;
                case "key_4":
                    label.setText(label.getText().concat(isShiftPressed ? "!" : "4"));
                    break;
                case "key_5":
                    label.setText(label.getText().concat(isShiftPressed ? "%" : "5"));
                    break;
                case "key_6":
                    label.setText(label.getText().concat(isShiftPressed ? "/" : "6"));
                    break;
                case "key_7":
                    label.setText(label.getText().concat(isShiftPressed ? "=" : "7"));
                    break;
                case "key_8":
                    label.setText(label.getText().concat(isShiftPressed ? "(" : "8"));
                    break;
                case "key_9":
                    label.setText(label.getText().concat(isShiftPressed ? ")" : "9"));
                    break;
                case "key_0":
                    label.setText(label.getText().concat("0"));
                    break;
                case "key_slash":
                    label.setText(label.getText().concat("/"));
                    break;
                case "key_period":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat(">"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? ":" : "."));
                    }
                    break;
                case "key_comma":
                    if (isControlPressed && isAltPressed) {
                        label.setText(label.getText().concat(";"));
                    } else {
                        label.setText(label.getText().concat(isShiftPressed ? "?" : ","));
                    }
                    break;
                case "key_backspace":
                    String text = label.getText();
                    if (text.length() > 0) {
                        label.setText(text.substring(0, text.length() - 1));
                    }
                    break;
                case "key_minus":
                    label.setText(label.getText().concat("-"));
                    break;
                case "key_plus":
                    label.setText(label.getText().concat("+"));
                    break;
                case "key_divide":
                    label.setText(label.getText().concat("/"));
                    break;
                case "key_multiply":
                    label.setText(label.getText().concat("*"));
                    break;
                case "key_decimal":
                    label.setText(label.getText().concat("."));
                    break;
                case "key_num_0":
                    label.setText(label.getText().concat("0"));
                    break;
                case "key_num_1":
                    label.setText(label.getText().concat("1"));
                    break;
                case "key_num_2":
                    label.setText(label.getText().concat("2"));
                    break;
                case "key_num_3":
                    label.setText(label.getText().concat("3"));
                    break;
                case "key_num_4":
                    label.setText(label.getText().concat("4"));
                    break;
                case "key_num_5":
                    label.setText(label.getText().concat("5"));
                    break;
                case "key_num_6":
                    label.setText(label.getText().concat("6"));
                    break;
                case "key_num_7":
                    label.setText(label.getText().concat("7"));
                    break;
                case "key_num_8":
                    label.setText(label.getText().concat("8"));
                    break;
                case "key_num_9":
                    label.setText(label.getText().concat("9"));
                    break;
                case "key_num_enter":
                    label.setText(label.getText().concat("\n"));
                    break;
                case "key_tab":
                    label.setText(label.getText().concat("    "));
                    break;

                default:
                    break;
            }
        }
    };*/

}
