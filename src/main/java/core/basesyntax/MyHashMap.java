package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREMENT = 2;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {

        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        putValue(newNode);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int position = getIndex(key);
        if (key == null || (table[position].next == null && key.equals(table[position].key))) {
            return table[position].value;
        } else {
            Node<K, V> tempNode = table[position];
            while (tempNode != null) {
                if (key.equals(tempNode.key)) {
                    return tempNode.value;
                }
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key != null) {
            return hash(key) % capacity;
        } else {
            return 0;
        }
    }

    private void initialization() {
        if (size == 0) {
            capacity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = (Node<K, V>[]) new Node[capacity];
        }
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        capacity = capacity * DEFAULT_INCREMENT;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (int j = 0; j < oldTable.length; j++) {
            Node<K, V> tempNode = oldTable[j];
            if (tempNode != null) {
                putValue(tempNode);
                if (tempNode.next != null) {
                    tempNode = tempNode.next;
                    do {
                        putValue(tempNode);
                        tempNode = tempNode.next;
                    } while (tempNode != null);
                }
            }
        }
    }

    private void putValue(Node<K, V> node) {
        Node<K, V> newNode = new Node<>(node.hash, node.key, node.value, null);
        int position = getIndex(node.key);
        if (table[position] == null) {
            table[position] = newNode;
        } else if (newNode.key == null
                || (newNode.hash == table[position].hash
                && newNode.key.equals(table[position].key))) {
            table[position].value = newNode.value;
            return;
        } else {
            Node<K, V> tempNode = table[position];
            int i = 0;
            do {
                if (tempNode.next == null) {
                    tempNode.next = newNode;
                    i = 1;
                } else {
                    tempNode = tempNode.next;
                }
            } while (i == 0);
        }
        size++;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(1 + key.hashCode());
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
