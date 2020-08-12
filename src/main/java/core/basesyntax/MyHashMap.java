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
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (LOAD_FACTOR * capacity);

    public MyHashMap() {
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currNode;
        int index = calculateIndex(hashGen(key), table.length);
        currNode = table[index];
        if (currNode != null) {
            while (currNode != null) {
                if (currNode.key == key || key != null && key.equals(currNode.key)) {
                    currNode.value = value;
                    return;
                }
                currNode = currNode.next;
            }
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
        if (size == threshold) {
            resizeTable();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currNode;
        currNode = table[calculateIndex(hashGen(key), table.length)];
        if (currNode != null) {
            while (currNode != null) {
                if (currNode.key == key || (key != null && key.equals(currNode.key))) {
                    return currNode.value;
                }
                currNode = currNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashGen(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        return hash ^ (hash >>> 7) ^ (hash >>> 4);
    }

    private int calculateIndex(int keyHash, int tableLength) {
        return keyHash & (tableLength - 1);
    }

    private void resizeTable() {
        capacity <<= 1;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        threshold = (int) (capacity * LOAD_FACTOR);
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        Node<K, V> currNode;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                currNode = oldTable[i];
                while (currNode != null) {
                    put(currNode.key, currNode.value);
                    currNode = currNode.next;
                }
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
