package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    protected MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getHash(key);
        resize();
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(getHash(key), key, value, null);
            size++;
            return;
        }

        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(getHash(key), key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getHash(key);
        Node<K, V> neededNode = table[bucketIndex];
        while (neededNode != null) {
            if (Objects.equals(neededNode.key, key)) {
                return neededNode.value;
            }
            neededNode = neededNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            int newCapacity = table.length * 2;
            Node<K, V>[] oldTable = table;
            table = new Node[newCapacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                    size--;
                }
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
