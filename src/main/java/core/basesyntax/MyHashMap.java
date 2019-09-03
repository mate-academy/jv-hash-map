package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        size = 0;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        boolean counter = true;
        int hash = hash(key);
        int index = hash % capacity;
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null, hash);
            size++;
        } else {
            Node<K, V> oldKey = table[index];
            while (oldKey != null) {
                if (hash == oldKey.hash) {
                    if (key == null && key == oldKey.key) {
                        oldKey.value = value;
                        counter = false;
                        break;
                    } else if (key.equals(oldKey.key)) {
                        oldKey.value = value;
                        counter = false;
                        break;
                    }
                }
                oldKey = oldKey.next;
            }
            if (counter) {
                Node<K, V> newNode = new Node<>(key, value, table[index], hash);
                table[index] = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash % capacity;
        Node<K, V> result = table[index];
        while (result != null) {
            if (hash == result.hash) {
                if (key == null && key == result.key) {
                    return result.value;
                } else if (key.equals(result.key)) {
                    return result.value;
                }
            }
            result = result.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() >>> 2;
    }

    private void resize() {
        if (capacity > Integer.MAX_VALUE / 2) {
            return;
        }
        Node<K, V>[] newTable = new Node[capacity * 2];
        for (int i = 0; i < capacity; i++) {
            Node<K, V> oldNode = table[i];
            while (oldNode != null) {
                boolean counter = true;
                int hash = hash(oldNode.key);
                int index = hash % capacity;
                if (newTable[index] == null) {
                    newTable[index] = new Node<>(oldNode.key, oldNode.value, null, hash);
                } else {
                    Node<K, V> oldKey = newTable[index];
                    while (oldKey != null) {
                        if (oldNode.key.equals(oldKey.key)) {
                            oldKey.value = oldNode.value;
                            counter = false;
                            break;
                        }
                        oldKey = oldKey.next;
                    }
                    if (counter) {
                        Node<K, V> newNode = new Node<>(oldNode.key,
                                oldNode.value, newTable[index], hash);
                        newTable[index] = newNode;
                    }
                }
                oldNode = oldNode.next;
            }
        }
        table = newTable;
        capacity *= 2;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private int hash;

        private Node(K key, V value, Node<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
