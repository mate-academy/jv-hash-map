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

        private Node(int hash, K key, V value, Node<K, V> next) {
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
        resize();
        Node<K,V> newNode = new Node(hash(key), key, value, null);
        if (table[hash(key)] == null) {
            table[hash(key)] = newNode;
            size++;
            return;
        }
        Node<K,V> currentNode = findNode(key);
        if (checkEquals(currentNode, key)) {
            currentNode.value = newNode.value;
            return;
        }
        size++;
        currentNode.next = newNode;
    }

    @Override
    public V getValue(K key) {
        return table[hash(key)] == null ? null : findNode(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == currentThreshold) {
            size = 0;
            currentThreshold *= 2;
            currentCapacity *= 2;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[currentCapacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }

    private boolean checkEquals(Node<K,V> currentNode, K key) {
        return key == currentNode.key || (key != null && key.equals(currentNode.key));
    }

    private Node<K,V> findNode(K key) {
        Node<K,V> currentNode;
        for (currentNode = table[hash(key)]; currentNode.next != null;
                 currentNode = currentNode.next) {
            if (checkEquals(currentNode, key)) {
                return currentNode;
            }
        }
        return currentNode;
    }
}
