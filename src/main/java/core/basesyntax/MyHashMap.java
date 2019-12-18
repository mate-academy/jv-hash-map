package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private double loadFactor;
    private int capacity;
    Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        capacity = DEFAULT_CAPACITY;
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public MyHashMap(int capacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = capacity;
        this.table = (Node<K,V>[]) new Node[capacity];
        this.size = 0;
    }

    private int indexFor(int hash, int tableCapacity) {
        return Math.abs(hash % tableCapacity);
    }

    private void addNode(int hash, K key, V value, int index) {
        if (size >= table.length * loadFactor) {
            resize();
        }
        Node<K, V> newNode = new Node<K, V>(hash, key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            newNode.next = table[index];
            table[index] = newNode;
        }
        size++;
    }

    private void putForNullKey(V value) {
        for (Node<K, V> item = table[0]; item != null; item = item.next) {
            if (item.key == null) {
                item.value = value;
                return;
            }
        }
        addNode(0, null, value, 0);
    }

    private void resize() {
        size = 0;
        capacity = capacity * 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[capacity];
        transfer(oldTable);
    }

    public void transfer(Node<K, V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = indexFor(key.hashCode(), capacity);
        for (Node<K, V> item = table[index]; item != null; item = item.next) {
            if (item.key == key || key.equals(item.key)) {
                item.value = value;
                return;
            }
        }
        addNode(key.hashCode(), key, value, index);

    }

    @Override
    public V getValue(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = indexFor(key.hashCode(), capacity);
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key || (key != null && key.equals(currentNode.key))) {
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
}

