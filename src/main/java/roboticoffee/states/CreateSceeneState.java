package roboticoffee.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Bence
 */
public class CreateSceeneState extends AbstractAppState {

    private final Node rootNode;
    private final Node localRootNode = new Node("BaseNode");
    private final AssetManager assetManager;

    public CreateSceeneState(SimpleApplication app) {
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        ((SimpleApplication) app).getFlyByCamera().setEnabled(false);

        app.getCamera().setLocation(new Vector3f(12, 8, -10));
        app.getCamera().lookAt(new Vector3f(8, -2, 10), Vector3f.UNIT_Y);
        app.getCamera().setFov(50);

        rootNode.attachChild(localRootNode);
        Node Shop = createShop();
        localRootNode.attachChild(Shop);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection(new Vector3f(1, -1, 1).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        rootNode.addLight(sun2);

        app.getViewPort().setBackgroundColor(ColorRGBA.fromRGBA255(204, 255, 255, 255));
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);

        super.cleanup();
    }

    private Node createShop() {
        Node shop = new Node("Shop");
        Spatial shopModel = assetManager.loadModel("Models/Epulet.glb");
        shopModel.setLocalRotation(new Quaternion().fromAngles(0, FastMath.PI, 0));
        shopModel.setLocalTranslation(0, 0, 0);
        shop.attachChild(shopModel);
        return shop;
    }

}
