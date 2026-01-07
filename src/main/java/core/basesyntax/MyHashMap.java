package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node[] table;
    private int size;

    public boolean keyEquals(K k1, K k2) {
        return k1 == null ? k2 == null : k1.equals(k2);
    }

    public void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[DEFAULT_CAPACITY];
        }
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int hash = key == null ? 0 : key.hashCode();
        int index = Math.abs(hash % table.length);

        Node<K, V> current = table[index];

        if (current == null) {
            table[index] = new Node(key, value, null);
            size++;
            return;
        }

        Node<K, V> prev = null;
        while (current != null) {
            if (keyEquals(current.key, key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = new Node(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash = key == null ? 0 : key.hashCode();
        int index = Math.abs(hash % table.length);

        Node<K, V> val = table[index];
        while (val != null) {
            if (keyEquals(key, val.key)) {
                return val.value;
            }
            val = val.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
