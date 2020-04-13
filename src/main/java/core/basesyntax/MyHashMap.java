package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private int tableCapacity;

    MyHashMap(int capacity) {
        this.size = 0;
        table = new Node[size];
        tableCapacity = capacity;
    }

    MyHashMap() {
        this.size = 0;
        this.table = new Node[DEFAULT_CAPACITY];
        tableCapacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int location = (key == null ? 0 : getLocation(key));
        Node<K, V> newNode = new Node(key, value);
        if (table[location] == null) {
            addNew(newNode, location);
        } else {
            addToLIst(newNode, location);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getLocation(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getLocation(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % tableCapacity);
    }

    private void addNew(Node<K, V> node, int location) {
        table[location] = node;
        size++;
    }

    private void addToLIst(Node<K, V> node, int location) {
        Node currentNode = table[location];
        while (currentNode != null) {
            if (currentNode.key == node.key
                    || currentNode.key != null && currentNode.key.equals(node.key)) {
                currentNode.value = node.value;
                return;
            }
            currentNode = currentNode.next;
        }
        node.next = table[location];
        table[location] = node;
        size++;
    }

    private void resize() {
        if (LOAD_FACTOR * tableCapacity == size) {
            Node<K, V>[] newTable = new Node[tableCapacity * 2];
            tableCapacity = newTable.length;
            size = 0;
            Node<K, V> []tableCopy = table;
            table = newTable;
            for (Node<K, V> node : tableCopy) {
                Node<K, V> currentNode = node;
                while (currentNode != null) {
                    int newLocation = getLocation(currentNode.key);
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}



