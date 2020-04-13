package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_FACTOR) {
            resize();
        }
        putVal(key, value, table, true);
    }

    @Override
    public V getValue(K key) {
        int newIndex = getIndex(key, table.length);
        if (table[newIndex] != null) {
            Node<K, V> node = table[newIndex];
            if (node != null) {
                do {
                    if (compareKey(node.key, key)) {
                        return node.value;
                    }
                    node = node.next;
                } while (node != null);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() >= 0 ? key.hashCode() : key.hashCode() * (-1);
    }

    private int getIndex(K key, int length) {
        return hash(key) % length;
    }

    private boolean compareKey(K key1, K key2) {
        return hash(key1) == hash(key2) && (key1 == key2) || key1 != null
                && key1.equals(key2);
    }

    private void putVal(K key, V value, Node<K, V>[] table, boolean evict) {
        int index = getIndex(key, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size = evict ? size + 1 : size;
            return;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (compareKey(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size = evict ? size + 1 : size;
                return;
            } else {
                node = node.next;
            }
        }
    }

    private void resize() {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            while (table[i] != null) {
                putVal(table[i].key, table[i].value, newTable, false);
                table[i] = table[i].next;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K keys, V value, Node<K, V> nextNode) {
            this.key = keys;
            this.value = value;
            this.next = nextNode;
        }
    }
}
