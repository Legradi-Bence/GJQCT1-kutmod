/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package roboticoffee.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Bence
 */
public class RobotState extends AbstractAppState {

    private final Node rootNode;
    private final Node localRootNode = new Node("BaseRobotNode");
    private final AssetManager assetManager;
    private Integer robotPosX = 0;
    private Integer robotPosZ = 0;




    public RobotState(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        rootNode.attachChild(localRootNode);

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Robot", b);
        geom.setLocalTranslation(0, 1, 0);
        Material mat = assetManager.loadMaterial("Materials/SecondBaseMaterial.j3m");
        geom.setMaterial(mat);
        localRootNode.attachChild(geom);

        setupKeys(app);
        


    }

    private void setupKeys(Application app) {
        app.getInputManager().addMapping("MoveUp", new KeyTrigger(KeyInput.KEY_W));
        app.getInputManager().addMapping("MoveDown", new KeyTrigger(KeyInput.KEY_S));
        app.getInputManager().addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_A));
        app.getInputManager().addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_D));

        app.getInputManager().addListener(actionListener, "MoveUp", "MoveDown", "MoveLeft", "MoveRight");
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (!isPressed) {
                return;
            }

            if (name.equals("MoveUp") && robotPosZ < 19) {
                localRootNode.move(0, 0, 2);
                robotPosZ++;
            } else if (name.equals("MoveDown") && robotPosZ > 0) {
                localRootNode.move(0, 0, -2);
                robotPosZ--;
            } else if (name.equals("MoveLeft") && robotPosX < 19) {
                localRootNode.move(2, 0, 0);
                robotPosX++;
            } else if (name.equals("MoveRight") && robotPosX > 0) {
                localRootNode.move(-2, 0, 0);
                robotPosX--;
            }
        }
    };

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);
    }

}
