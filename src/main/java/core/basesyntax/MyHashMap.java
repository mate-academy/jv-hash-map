package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_LENGTH = 16;

    private Node[] table;
    private int size;

    public MyHashMap() {
        this.size = 0;
        this.table = new Node[DEFAULT_LENGTH];
    }

    private int getHash1(K key) {
        if (key == null) {
            return 0;
        }
        int k = Math.abs(key.hashCode());
        k ^= (k >>> 20) ^ (k >>> 12) + 23;
        return k ^ (k >>> 7) ^ (k >>> 4) + 31;
    }

    private int getIndex(int hash) {
        return hash % table.length;
    }

    private void checkCapacity() {
        if (size >= table.length * LOAD_FACTOR) {
            grow();
        }
    }

    private boolean checkBucketValue(int index) {
        return (table[index] == null) || (table[index].deleted);
    }

    private boolean checkBucket(int index, int hash, K key) {
        return ((checkBucketValue(index))
                || (hash == table[index].hash && Objects.equals(table[index].key, key)));
    }

    private void grow() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        int hash = getHash1(key);
        int index = getIndex(hash);
        while (index < table.length) {
            if (checkBucket(index, hash, key)) {
                size += checkBucketValue(index) ? 1 : 0;
                table[index] = new Node<K, V>(hash, key, value);
                return;
            }
            index++;
        }
        grow();
        put(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = getHash1(key);
        int index = getIndex(hash);
        while (index < table.length) {
            if (!checkBucketValue(index) && Objects.equals(table[index].key, key)) {
                return (V) table[index].value;
            }
            index++;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private boolean deleted;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.deleted = false;
        }
    }

}
