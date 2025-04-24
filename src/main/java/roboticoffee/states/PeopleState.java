package roboticoffee.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import roboticoffee.utils.LimitedQueue;
import roboticoffee.utils.Order;
import roboticoffee.utils.Person;
import roboticoffee.utils.Table;

public class PeopleState extends AbstractAppState {
    
    private final SimpleApplication simpleApp;
    private final Node rootNode;
    private final Node localRootNode = new Node("PeopleNode");
    private List<Table> tables = new ArrayList<>();
    private List<Person> people = new ArrayList<>();
    private LimitedQueue<Person> queue = new LimitedQueue<>(5);
    private Vector3f counterPosition = new Vector3f(5, 0, 1);
    private float spawnTimeInterval = 1;
    Random random = new Random();

    public PeopleState(SimpleApplication app) {
        simpleApp = app;
        rootNode = app.getRootNode();
        rootNode.attachChild(localRootNode);
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
        for (Person person : people) {
            person.update(tpf);
        }
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
            person = new Person(localRootNode, simpleApp.getAssetManager().loadModel("Models/Ferfiallo.glb"), name, x, z);
        } else {
            person = new Person(localRootNode, simpleApp.getAssetManager().loadModel("Models/Noallo.glb"), name, x, z);
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

    public void addTable(String name, int x, int z) {

        Table table = new Table(localRootNode, simpleApp.getAssetManager().loadModel("Models/Szekesasztal.glb"), name, x, z);
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


    
}
