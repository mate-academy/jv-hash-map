package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MULTIPLICATION_FACTOR = 2;

    private Node<K, V> next;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        addNode(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = key == null ? 0 : indexOfMapArray(hash);
        Node<K, V> nodeSearch = table[index];
        while (nodeSearch != null) {
            if (Objects.equals(nodeSearch.key, key)) {
                return nodeSearch.value;
            }
            nodeSearch = nodeSearch.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size >= (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            Node<K, V>[] copiedTable = table;
            table = (Node<K, V>[]) new Node[copiedTable.length * MULTIPLICATION_FACTOR];
            for (int i = 0; i < copiedTable.length; i++) {
                while (copiedTable[i] != null) {
                    addNode(copiedTable[i].key, copiedTable[i].value);
                    copiedTable[i] = copiedTable[i].next;
                }
            }
        }
    }

    private void addNode(K key, V value) {
        int hash = hash(key);
        int index = key == null ? 0 : indexOfMapArray(hash);
        Node<K, V> node = new Node<>(hash, key, value, null);
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> nodeSearch = table[index];
            while (nodeSearch.next != null || Objects.equals(nodeSearch.key, key)) {
                if (Objects.equals(nodeSearch.key, key)) {
                    size--;
                    nodeSearch.value = value;
                    return;
                }
                nodeSearch = nodeSearch.next;
            }
            nodeSearch.next = node;
        }
    }

    private int indexOfMapArray(int hash) {
        int index = hash % table.length;
        return index;
    }

    private int hash(K key) {
        return Math.abs(31 * 17 + (key == null ? 0 : key.hashCode()));
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private int hash;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
