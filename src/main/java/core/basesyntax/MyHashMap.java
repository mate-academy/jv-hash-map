package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int START_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] hashTable;
    private int load;
    private int size;

    public MyHashMap() {
        this.hashTable = new Node[START_CAPACITY];
        this.load = (int) (START_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K, V> newHashTable = new Node<>(hash, key, value, null);
        Node<K,V> table = hashTable[index];
        if (table == null) {
            hashTable[index] = newHashTable;
        }
        while (table != null) {
            if (isHashAndKeysEquals(table, hash, key)) {
                table.value = value;
                return;
            }
            if (table.next == null) {
                table.next = newHashTable;
                break;
            }
            table = table.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K,V> table = hashTable[index];
        while (table != null) {
            if (isHashAndKeysEquals(table, hash, key)) {
                return table.value;
            }
            table = table.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode() % hashTable.length;
    }

    private int getIndex(K key) {
        return getHash(key) & hashTable.length - 1;
    }

    private void resize() {
        if (load == size) {
            size = 0;
            Node<K, V>[] oldHashTable = hashTable;
            hashTable = new Node[hashTable.length * GROW_FACTOR];
            load *= GROW_FACTOR;
            for (Node<K, V> table : oldHashTable) {
                while (table != null) {
                    put(table.key, table.value);
                    table = table.next;
                }
            }
        }
    }

    private boolean isHashAndKeysEquals(Node<K,V> table, int hash, K key) {
        return (table.hash == hash && Objects.equals(table.key, key));
    }
}
