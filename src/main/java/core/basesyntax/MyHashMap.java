package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] hashTable;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;

    MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
    }

    public int hash(K key) {
        return 31 * 17 + Math.abs(key.hashCode());
    }

    private int index(K key) {
        return key == null ? 0 : hash(key) % (hashTable.length - 1);
    }

    public void ensureCapacity() {
        if (size >= hashTable.length * LOAD_FACTOR) {
            Node<K, V>[] twinHashTable = hashTable;
            hashTable = new Node[hashTable.length * 2];
            size = 0;
            for (int i = 0; i < twinHashTable.length; i++) {
                Node<K, V> node = twinHashTable[i];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        int index = index(key);
        Node<K, V> entry = hashTable[index];
        while (entry != null) {
            if (Objects.equals(key, entry.key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        Node<K, V> entryInNewBucket = new Node(key, value, hashTable[index(key)]);
        hashTable[index(key)] = entryInNewBucket;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = index(key);
        Node<K, V> node = hashTable[index];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
}
