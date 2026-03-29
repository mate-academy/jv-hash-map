package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    public void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key, int length) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % length;
    }

    private boolean keysAreEqual(K firstKey, K secondKey) {
        if (firstKey == null && secondKey == null) {
            return true;
        }
        if (firstKey == null || secondKey == null) {
            return false;
        }
        return firstKey.equals(secondKey);
    }

    @Override
    public void put(K key, V value) {
        if ((size + 1) > table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (keysAreEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (keysAreEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
