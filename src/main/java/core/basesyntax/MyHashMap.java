package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            size = addEntry(Objects.hashCode(key), key, value, getIndex(key)) ? size + 1 : size;
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(Object key) {
        return (key == null) ? 0 : Objects.hashCode(key) & table.length - 1;
    }

    private void resize() {
        if (size >= threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            threshold = table.length << 1;
            size = 0;
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] table) {
        for (Node<K, V> kvNode : table) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private void putForNullKey(V value) {
        Node<K, V> currentNode = table[0];
        while (currentNode != null) {
            if (currentNode.key == null) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        addEntry(0, null, value, 0);
        size++;
    }

    private boolean addEntry(int hash, K key, V value, int index) {
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value);
            return true;
        }
        while (true) {
            if (currentNode.hash == hash && (currentNode.key == key || Objects.equals(key, currentNode.key))) {
                currentNode.value = value;
                return false;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hash, key, value);
                return true;
            }
            currentNode = currentNode.next;
        }
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next = null;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
