package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
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
        Node<K, V> node = table[getIndex(key, table.length)];
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
        return (getHash(key1) == getHash(key2))
                && (key1 == null ? key2 == null : key1.equals(key2));
    }

    private int getHash(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        return hashCode >= 0 ? hashCode : hashCode * (-1);
    }

    private int getIndex(K key, int length) {
        return getHash(key) % length;
    }

    private void putNode(K key, V value, Node<K, V>[] table, boolean increment) {
        int index = getIndex(key, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size = increment ? size + 1 : size;
            return;
        }
        Node<K, V> oldNode = table[index];
        while (oldNode != null) {
            if (compareKeys(oldNode.key, key)) {
                oldNode.value = value;
                return;
            }
            if (oldNode.next == null) {
                oldNode.next = new Node<>(key, value, null);
                size = increment ? size + 1 : size;
                return;
            } else {
                oldNode = oldNode.next;
            }
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
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
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
