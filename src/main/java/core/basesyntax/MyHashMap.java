package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_RATE = 2;

    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            Node<K, V> lastNode = null;
            while (currentNode != null) {
                if (keyEquals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                lastNode = currentNode;
                currentNode = currentNode.next;
            }
            lastNode.next = newNode;
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> result = table[index];
        while (result != null) {
            if (keyEquals(result.key, key)) {
                return result.value;
            }
            result = result.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return Math.abs(hash) % (int) (threshold / DEFAULT_LOAD_FACTOR);
    }

    private boolean keyEquals(K key1, K key2) {
        return (key1 == key2) || (key1 != null && key1.equals(key2));
    }

    private void resize() {
        int capacity = table.length * INCREASE_RATE;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> temp = node.next;
                node.next = null;
                int newIndex = getIndex(node.key);

                if (newTable[newIndex] == null) {
                    newTable[newIndex] = node;
                } else {
                    Node<K, V> lastNode = newTable[newIndex];
                    while (lastNode.next != null) {
                        lastNode = lastNode.next;
                    }
                    lastNode.next = node;
                }
                node = temp;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
