package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int ARRAY_LENGTH_INCREASE_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (checkLoadOfMap()) {
            resizeNodesArray();
        }
        int index = getIndexByKey(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            insertValue(newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (equalsKeys(key, currentNode.key)) {
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

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void insertValue(Node<K, V> newNode) {
        Node<K, V> currentNode = table[getIndexByKey(newNode.key)];
        Node<K, V> previousNode = null;
        while (currentNode != null) {
            if (equalsKeys(currentNode.key, newNode.key)) {
                currentNode.value = newNode.value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        currentNode = newNode;
        previousNode.next = currentNode;
        size++;
    }

    private boolean checkLoadOfMap() {
        return size == threshold;
    }

    private void resizeNodesArray() {
        int tableSize = ARRAY_LENGTH_INCREASE_FACTOR * table.length;
        threshold = (int) (tableSize * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[tableSize];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null) {
                continue;
            }
            put(oldTable[i].key, oldTable[i].value);
            while (oldTable[i].next != null) {
                oldTable[i] = oldTable[i].next;
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    private boolean equalsKeys(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }
}
