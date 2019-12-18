package core.basesyntax;

import static java.lang.Math.abs;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K, V>[] hashTable;
    private int size;
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    public MyHashMap() {
        hashTable = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        extendSize();
        Node<K, V> newNode = new Node<K, V>(key, value, null);
        int index = (key == null) ? 0 : newNode.hashCode() % hashTable.length;
        Node<K, V> node = hashTable[index];

        if (hashTable[index] == null) {
            node = new Node<K, V>(key, value, null);
            hashTable[index] = node;
            size++;
            return;
        }

        while (true != false) {
            if (Objects.equals(node.key, newNode.key)) {
                node.value = newNode.value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {

        int index = (key == null) ? 0 : hash(key);
        if (index < hashTable.length && hashTable[index] != null) {

            Node<K, V> noda = hashTable[index];
            while (noda != null) {
                if (Objects.equals(noda.key, key)) {
                    return noda.value;
                }
                noda = noda.next;
            }
        }
        return null;
    }

    private int hash(K key) {
        int hash = abs(31 * key.hashCode());
        return (hashTable.length == 0 || hash == 0) ? 0 : hash % hashTable.length;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {

        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return key.equals(node.key);
        }

        @Override
        public int hashCode() {
            return Math.abs(key.hashCode() * 31);
        }
    }

    private void arrayResizing() {
        Node<K, V>[] oldHashArray = hashTable;
        hashTable = new Node[oldHashArray.length << 1];
        size = 0;
        for (Node<K, V> node : oldHashArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }

        }
    }

    private void extendSize() {
        if (size > hashTable.length * LOAD_FACTOR) {
            arrayResizing();
        }
    }
}
