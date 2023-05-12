package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(key, value);
        int index = getIndexByKeyHash(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> current = table[index];
        while (current != null) {
            if (areKeysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = node;
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKeyHash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (areKeysEqual(node.key, key)) {
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
        if (size < DEFAULT_LOAD_FACTOR * table.length) {
            return;
        }
        size = 0;
        Node<K, V>[] previousTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node : previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndexByKeyHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private boolean areKeysEqual(K first, K second) {
        return first == second || first != null && first.equals(second);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

