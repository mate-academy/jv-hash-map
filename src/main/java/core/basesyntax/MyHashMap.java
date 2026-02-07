package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (areKeysEqual(current.key, key)) {
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
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (areKeysEqual(current.key, key)) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.floorMod(key.hashCode(), table.length);
    }

    private boolean areKeysEqual(K firstKey, K secondKey) {
        return firstKey == secondKey || (firstKey != null && firstKey.equals(secondKey));
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = createTable(oldTable.length * RESIZE_MULTIPLIER);
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
