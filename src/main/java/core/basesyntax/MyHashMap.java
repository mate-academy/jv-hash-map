package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        int inputKeyHash = Objects.hashCode(key);
        Node<K,V> inputNode = new Node<>(key, value, null);
        Node<K,V> currentNode = table[index];

        if (currentNode == null) {
            table[index] = inputNode;
        }

        while (currentNode != null) {
            Node<K,V> nextNode = currentNode.next;
            if (Objects.hashCode(currentNode.key) == inputKeyHash
                    && (currentNode.key == key || (key != null && key.equals(currentNode.key)))) {
                currentNode.value = value;
                return;
            } else if (nextNode == null) {
                currentNode.next = inputNode;
            }
            currentNode = nextNode;
        }

        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        final int index = getIndex(key);
        final int inputKeyHash = Objects.hashCode(key);
        Node<K,V> currentNode = table[index];

        while (currentNode != null) {
            if (Objects.hashCode(currentNode.key) == inputKeyHash
                        && currentNode.key == key
                        || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            } else {
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        final Node<K,V>[] oldTab = table;
        table = (Node<K,V>[]) new Node[(table.length << 1)];
        threshold *= 2;
        size = 0;

        transfer(oldTab);
    }

    private void transfer(Node<K, V>[] oldTab) {
        for (Node<K,V> currentNode : oldTab) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    public int getIndex(Object key) {
        return (key == null) ? 0 : ((Objects.hashCode(key) & table.length - 1));
    }

    public static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
