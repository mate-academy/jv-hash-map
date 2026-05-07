package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INIT_CAP = 16;
    private static final double MAX_SIZE = 0.75;
    private int capacity;
    private int size;
    private double threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INIT_CAP];
        capacity = INIT_CAP;
        threshold = INIT_CAP * MAX_SIZE;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }

        while (node != null) {
            if ((key == node.key) || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == node.key) || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int oldCapacity = capacity;
        capacity += capacity;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];

            while (node != null) {
                Node<K, V> next = node.next;
                put(node.key, node.value);
                node = next;
            }
        }
        threshold = capacity * MAX_SIZE;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
