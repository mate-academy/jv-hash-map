package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int INCREASE_SIZE_OF_ARR_TO = 2;
    private Node<K, V>[] table;
    private int size;
    private boolean isResizing = false;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key, table.length);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
            if (!isResizing) {
                size++;
            }
        } else {
            while (node.next != null || checkKey(key, node)) {
                if (checkKey(key, node)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = newNode;
            if (!isResizing) {
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key, table.length);
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (checkKey(key, node)) {
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
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            isResizing = true;
            int newCapacity = table.length * INCREASE_SIZE_OF_ARR_TO;
            Node<K, V>[] newTable = new Node[newCapacity];
            Node<K, V>[] tempTable = table;
            table = newTable;
            for (Node<K, V> node : tempTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
            isResizing = false;
        }
    }

    private boolean checkKey(K key, Node<K, V> node) {
        if (node.key == key || node.key != null && node.key.equals(key)) {
            return true;
        }
        return false;
    }

    private int hash(K key, int capacity) {
        return Math.abs((key == null) ? 0 : key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
