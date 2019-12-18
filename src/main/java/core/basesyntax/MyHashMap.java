package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private Node<K, V>[] hashTable;
    private float loadFactor;
    private int size;

    public MyHashMap() {
        hashTable = new Node[CAPACITY];
        loadFactor = 0.75f;
        size = 0;
    }

    private int hash(K key) {
        int hash = 31;
        hash = hash * 17 + key.hashCode();
        hash = hash % hashTable.length;
        return hash < 0 ? -1 * hash : hash;
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        private int hash() {
            int hash = hashCode() % hashTable.length;
            return hash < 0 ? -1 * hash : hash;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private Node<K, V> getNext() {
            return next;
        }

        private void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) o;
            return Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    || Objects.equals(hash, node.hashCode());
        }

        @Override
        public int hashCode() {
            int hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= hashTable.length * loadFactor) {
            resizeHashMap();
        }
        Node<K, V> newNode = new Node<>(key, value);
        if (key == null) {
            if (hashTable[0] == null) {
                hashTable[0] = newNode;
                size++;
                return;
            }
            Node<K, V> node = hashTable[0];
            while (node != null) {
                if (node.getKey() == null && !Objects.equals(value, node.getValue())) {
                    node.setValue(value);
                    return;
                }
                if (node.getKey() != null && node.next == null) {
                    node.next = newNode;
                    size++;
                    return;
                }
                node = node.next;
            }
        }
        int index = newNode.hash();
        if (hashTable[index] == null) {
            simpleAdd(index, newNode);
            return;
        }
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(key, node.getKey())) {
                node.setValue(value);
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
            }
            node = node.next;
        }
    }

    public void simpleAdd(int index, Node<K, V> newNode) {
        hashTable[index] = newNode;
        size++;
    }

    private void resizeHashMap() {
        int capacity = hashTable.length;
        if (capacity == MAX_CAPACITY) {
            return;
        }
        if (capacity << 1 >= MAX_CAPACITY) {
            capacity = MAX_CAPACITY;
        } else {
            capacity = capacity << 1;
        }
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldHashTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> node = hashTable[0];
            if (node.getKey() == null) {
                return node.getValue();
            }
            while (node != null) {
                if (node.getKey() == null) {
                    return node.getValue();
                }
                node = node.next;
            }
        }
        int index = hash(key);
        if (index < hashTable.length && hashTable[index] != null) {
            Node<K, V> node = hashTable[index];
            while (node != null) {
                if (Objects.equals(key, node.getKey())) {
                    return node.getValue();
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
}
