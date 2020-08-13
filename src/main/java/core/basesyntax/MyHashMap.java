package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;

    private int size;
    private Node<K, V>[] table;
    private int threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> currNode = table[index];
        while (currNode != null) {
            if (currNode.key == key || key != null && key.equals(currNode.key)) {
                currNode.value = value;
                return;
            }
            currNode = currNode.next;
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
        if (size == threshold) {
            resizeTable();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currNode = table[calculateIndex(key)];
        while (currNode != null) {
            if (currNode.key == key || (key != null && key.equals(currNode.key))) {
                return currNode.value;
            }
            currNode = currNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() & (table.length - 1);
    }

    private void resizeTable() {
        size = 0;
        Node<K, V> currNode;
        Node<K, V>[] oldTable = table;
        // Bitwise shift left on one position. Works same as table.length * 2
        table = new Node[table.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < oldTable.length; i++) {
            currNode = oldTable[i];
            while (currNode != null) {
                put(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    private static class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
