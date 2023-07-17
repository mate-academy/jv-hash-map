package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_NUMBER = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(node.key);
        if (table[index] == null) {
            table[index] = node;
        } else if (putIfKeyContainsInHashMap(node, index)) {
            return;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null && currentNode.key.equals(key)) {
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return key == null ? 0 : getHash(key) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newLength = oldTable.length * INCREASE_NUMBER;
        table = new Node[newLength];
        threshold = (int) (LOAD_FACTOR * table.length);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean putIfKeyContainsInHashMap(Node<K, V> node, int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == node.key
                    || currentNode.key != null && currentNode.key.equals(node.key)) {
                currentNode.value = node.value;
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
