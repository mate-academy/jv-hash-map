package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int INCREASE_INDEX = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

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

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            putInEmptyBucket(key, value, index);
        } else {
            putInNoEmptyBucket(key, value, index);
        }
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> temp = table[hash(key)];
        while (temp != null) {
            if (temp.key == key || key != null && key.equals(temp.key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] previousMap = table;
        table = new Node[table.length * INCREASE_INDEX];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node: previousMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putInEmptyBucket(K key, V value, int index) {
        table[index] = new Node<>(key, value, null);
        size++;
    }

    private void putInNoEmptyBucket(K key, V value, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                break;
            }
            node = node.next;
        }
        size++;
    }
}
