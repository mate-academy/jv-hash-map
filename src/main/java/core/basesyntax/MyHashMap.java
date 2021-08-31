package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int currentThreshold;
    private Node<K,V>[] table;
    private int size;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        currentThreshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> addedNode = new Node(hash(key), key, value, null);
        if (table[hash(key)] == null) {
            table[hash(key)] = addedNode;
            size++;
            return;
        }
        Node<K,V> currentNode = table[hash(key)];
        if (++size > currentThreshold) {
            resize();
        }
        do {
            if (key == currentNode.key || (key != null && !key.equals(currentNode.key))) {
                table[hash(key)] = addedNode;
                return;
            } else {
                currentNode.next = addedNode;
            }
            currentNode = currentNode.next;

        } while (currentNode.next != null);
    }

    @Override
    public V getValue(K key) {
        Node<K,V> nodeWeNeed = table[hash(key)];
        if (nodeWeNeed == null) {
           return null;
        }
        while ((key == nodeWeNeed.key || (key != null && !key.equals(nodeWeNeed.key))) && nodeWeNeed.next != null) {
            nodeWeNeed = nodeWeNeed.next;
        }
        return nodeWeNeed.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        currentThreshold *= 2;
        Node<K,V>[] resizedTable =  (Node<K,V>[]) new Node[(int) (currentCapacity * 2)];
        System.arraycopy(table, 0, resizedTable, 0, table.length - 1);
        table = resizedTable;
    }

    private Node<K,V> getNode(int hash, K key) {
        return null;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int hashOfKey = Math.abs(key.hashCode());
        while (hashOfKey > currentCapacity) {
            hashOfKey %= currentCapacity;

        }
        return hashOfKey;
    }
}
