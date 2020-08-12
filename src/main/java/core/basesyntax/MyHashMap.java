package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final double LOAD_FACTORY = 0.75;

    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTORY) {
            resize();
        }
        int index = key == null ? 0 : indexFor(hash(key.hashCode()));
        Node<K, V> temp = table[index];
        if (temp == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }

        while (true) {
            if (key != null ? key.equals(temp.key) : temp.key == null) {
                temp.value = value;
                return;
            }
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
        }
        temp.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : indexFor(hash(key.hashCode()));
        Node<K, V> temp = table[index];
        while (temp != null) {
            if (key != null ? key.equals(temp.key) : temp.key == null) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int indexFor(int hash) {
        return hash & (capacity - 1);
    }

    private void resize() {
        capacity = capacity * 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> temp = oldTable[i];
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
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
