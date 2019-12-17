package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    public MyHashMap(int capacity) {
        if (capacity < 0 || capacity > MAX_CAPACITY) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (capacity * DEFAULT_LOAD_FACTOR <= size) {
            resizeHashMap();
        }
        Node<K, V> current = table[indexOfHash(key)];
        if (current == null) {
            table[indexOfHash(key)] = new Node<>(key, value, null);
            size++;
            return;
        }

        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value, null);
                break;
            }
            current = current.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[indexOfHash(key)];
        if (current == null) {
            return null;
        }

        while (current != null) {
            if (key == current.key || (key != null && key.equals(current.key))) {
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

    private int indexOfHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % capacity);
    }

    private void resizeHashMap() {
        if (capacity == MAX_CAPACITY) {
            return;
        }
        if (capacity << 1 >= MAX_CAPACITY) {
            capacity = MAX_CAPACITY;
        } else {
            capacity = capacity << 1;
        }

        Node<K, V>[] oldHashMap = table;
        table = new Node[capacity];

        for (int i = 0; i < oldHashMap.length; i++) {
            Node<K, V> current = oldHashMap[i];
            if (current == null) {
                continue;
            }
            while (current != null) {
                put(current.key, current.value);
                size--;
                current = current.next;
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.hash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
