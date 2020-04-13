package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int newCapacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
        newCapacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {

        if ((double) size / table.length >= DEFAULT_LOAD_FACTOR) {
            resize();
        }
        putNode(key, value, table, true);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getKeyIndex(key)];
        if (node != null) {
            while (node != null) {
                if (compareKeys(key, node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean compareKeys(K key1, K key2) {
        return getHash(key1) == getHash(key2) && (key1 == null ? key2 == null : key1.equals(key2));
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getKeyIndex(K key) {
        return getHash(key) % newCapacity;
    }

    private void putNode(K key, V value, Node<K, V>[] table, boolean increment) {
        int index = getKeyIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size = increment ? size + 1 : size;
            return;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (compareKeys(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size = increment ? size + 1 : size;
                return;
            } else {
                node = node.next;
            }
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> cell : table) {
            Node<K, V> node = cell;
            while (node != null) {
                putNode(node.key, node.value, newTable, false);
                node = node.next;
            }
        }
        table = newTable;
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
