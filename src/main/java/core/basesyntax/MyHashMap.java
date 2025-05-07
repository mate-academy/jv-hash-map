package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int TABLE_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];

    class Node<K, V> {
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

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNodeForPut = new Node(hashCode(), key, value, null);
        int index = getHash(key) % table.length;
        if (table[index] == null) {
            table[index] = newNodeForPut;
            size++;
            return;
        } else {
            Node<K, V> currentNode = table[index];
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            while (currentNode.next != null) {
                if (key == currentNode.key || currentNode.key != null
                        && currentNode.key.equals(key)) {
                    currentNode.value = value;

                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNodeForPut;
        }
        size++;
    }

    private int getHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    @Override
    public V getValue(K key) {
        int index = getHash(key) % table.length;
        Node<K, V> currentNode = table[index];
        V value = null;
        while (currentNode != null) {
            if (key == currentNode.key || currentNode.key != null
                    && currentNode.key.equals(key)) {
                value = currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        size = 0;
        capacity = table.length * TABLE_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
    }
}

