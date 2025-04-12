package roboticoffee.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3tools.optimize.GeometryBatchFactory;

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

        app.getCamera().setLocation(new Vector3f(10, 15, -10));
        app.getCamera().lookAt(new Vector3f(10, 0, 10), Vector3f.UNIT_Y);
        

        rootNode.attachChild(localRootNode);
        Node Tiles = createTiles();
        localRootNode.attachChild(Tiles);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(ambient);

        app.getViewPort().setBackgroundColor(ColorRGBA.fromRGBA255(204, 255, 255, 255));
    }

    @Override
    public void cleanup() {
        rootNode.detachChild(localRootNode);

        super.cleanup();
    }

    private Node createTiles() {
        Node tiles = new Node("Tiles");
        Material mat = assetManager.loadMaterial("Materials/BaseMaterial.j3m");

        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                Spatial tile = assetManager.loadModel("Models/Tile.glb");
                tile.setLocalScale(0.5f, 0.5f, 0.5f);
                tile.setMaterial(mat);
                tile.setLocalTranslation(i, -0.1f, j);
                tiles.attachChild(tile);
            }
        }

        GeometryBatchFactory.optimize(tiles, true);
        return tiles;
    }

}
