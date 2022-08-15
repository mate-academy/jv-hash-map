package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int currentCapacity;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        currentCapacity = table.length;
    }

    @Override
    public void put(K key, V value) {
        addValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int keyHash = getHash(key);
        Node<K, V> node = table[keyHash];
        if (table[keyHash] != null && Objects.equals(node.key, key)) {
            return node.value;
        } else {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
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

    private void addValue(K key, V value) {
        resize();
        int keyHash = getHash(key);
        Node<K, V> newNode = new Node<>(keyHash, key, value, null);
        Node<K, V> oldNode = null;
        if (table[keyHash] == null) {
            table[keyHash] = newNode;
            size++;
        } else {
            Node<K, V> node = table[keyHash];
            if (Objects.equals(node.key, key)) {
                node.value = newNode.value;
                return;
            }
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = newNode.value;
                    return;
                }
                oldNode = node;
                node = node.next;
            }
            oldNode.next = newNode;
            size++;
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % currentCapacity);
    }

    private Node<K, V>[] resize() {
        if (size + 1 > threshold) {
            Node<K, V>[] newNodeArray = new Node[DEFAULT_CAPACITY * INCREASE_FACTOR];
            Node<K, V>[] oldTab = table;
            table = newNodeArray;
            threshold = threshold * INCREASE_FACTOR;
            for (int i = 0; i < oldTab.length; i++) {
                if (oldTab[i] != null) {
                    int newHash = getHash(oldTab[i].key);
                    if (table[newHash] == null) {
                        table[newHash] = oldTab[i];
                    } else {
                        Node<K, V> node = table[newHash].next;
                        while (node.next != null) {
                            node = node.next;
                        }
                        node.next = oldTab[i];
                    }
                }
            }
        }
        return table;
    }

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
}
