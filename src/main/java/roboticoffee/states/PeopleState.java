package roboticoffee.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import roboticoffee.utils.LimitedQueue;
import roboticoffee.utils.Person;
import roboticoffee.utils.Table;

public class PeopleState extends AbstractAppState {

    private final Node rootNode;
    private final Node localRootNode = new Node("PeopleNode");
    private List<Table> tables = new ArrayList<>();
    private List<Person> people = new ArrayList<>();
    private LimitedQueue<Person> queue = new LimitedQueue<>(5);
    private Vector3f counterPosition = new Vector3f(5, 0, 1);
    private Spatial maleStandingModel;
    private Spatial maleSittingModel;
    private Spatial femaleStandingModel;
    private Spatial femaleSittingModel;
    private Spatial coffeeModel;
    private Spatial tableModel;
    private float spawnTimeInterval = 1;
    Random random = new Random();

    public PeopleState(SimpleApplication simpleApp) {
        rootNode = simpleApp.getRootNode();
        rootNode.attachChild(localRootNode);
        maleStandingModel = simpleApp.getAssetManager().loadModel("Models/Ferfiallo.glb");
        maleSittingModel = simpleApp.getAssetManager().loadModel("Models/Ferfiulo.glb");
        femaleStandingModel = simpleApp.getAssetManager().loadModel("Models/Noallo.glb");
        femaleSittingModel = simpleApp.getAssetManager().loadModel("Models/Noulo.glb");
        coffeeModel = simpleApp.getAssetManager().loadModel("Models/Poharasztalon.glb");
        tableModel = simpleApp.getAssetManager().loadModel("Models/Szekesasztal.glb");

    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        spawnTimeInterval -= tpf;
        if (spawnTimeInterval <= 0) {
            generatePeople();
            spawnTimeInterval = 1;
        }
        updateQueuePositions();
        List<Person> toRemove = new ArrayList<>();
        for (Person person : people) {
            person.update(tpf);
            if (person.getLeft()) {
                toRemove.add(person);
                person.getTable().setOccupied(false);
            }
        }
        people.removeAll(toRemove);

    }

    private void spawnPerson(String name, int x, int z) {
        if (tables.isEmpty()) {
            return;
        }
        Table table;
        if ((table = getEmptyTable()) == null) {
            return;
        }
        table.setOccupied(true);
        Person person;
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(2);
        if (randomIndex == 0) {
            person = new Person(localRootNode, maleStandingModel.clone(), maleSittingModel.clone(), coffeeModel.clone(), name, x, z);
        } else {
            person = new Person(localRootNode, femaleStandingModel.clone(), femaleSittingModel.clone(), coffeeModel.clone(), name, x, z);
        }
        person.setTable(table);
        people.add(person);
        addToQueue(person);
    }

    private void generatePeople() {
        if (isAllTableOccupied()) {
            return;
        }
        if (queue.isFull()) {
            return;
        }
        int chance = random.nextInt(10);
        if (chance == 0) {
            spawnPerson("Person_" + people.size(), 0, 1);
        }

    }

    public void addToQueue(Person person) {
        queue.add(person);
        updateQueuePositions();
    }

    public boolean isEmptyQueue() {
        return queue.isEmpty();
    }

    private void updateQueuePositions() {
        int i = 0;
        for (Person person : queue) {
            Vector3f position = counterPosition.add(-i, 0, 0);
            person.setDestiantion(position);
            i++;
        }
    }

    public List<Table> getTables() {
        return tables;
    }

    public Table getTableByCoords(Vector3f position) {
        for (Table table : tables) {
            if (table.getPosition().equals(position)) {
                return table;
            }
        }
        return null;

    }

    public void addTable(String name, int x, int z) {

        Table table = new Table(localRootNode, tableModel.clone(), name, x, z);
        tables.add(table);
    }

    private boolean isAllTableOccupied() {
        for (Table table : tables) {
            if (!table.isOccupied()) {
                return false;
            }
        }
        return true;
    }

    private Table getEmptyTable() {
        for (Table table : tables) {
            if (!table.isOccupied()) {
                return table;
            }
        }
        return null;
    }

    public Person getFirstPerson() {
        if (queue.isEmpty()) {
            return null;
        }
        Person person = queue.getFirst();
        if (!person.isAtCounter()) {
            return null;
        }
        queue.remove(person);
        return person;
    }

    public boolean isAtTable(Vector3f position) {
        for (Table table : tables) {
            if (table.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public void leave(Table table) {
        for (Person person : people) {
            if (person.getTable() == table) {
                person.setLeaving(true);
                break;
            }
        }
    }

}
