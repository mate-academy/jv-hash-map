package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    public static final int GROWTH_FACTOR = 2;
    private int size;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= nodes.length * LOAD_FACTOR) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int position = hash % nodes.length;
        Node<K, V> currentNode = nodes[position];
        while (currentNode != null) {
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
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

    private static <K> int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        Node<K, V>[] temp = nodes;
        nodes = new Node[nodes.length * GROWTH_FACTOR];
        size = 0;
        for (Node<K, V> node : temp) {
            while (node != null) {
                putValue(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putValue(K key, V value) {
        int hash = getHash(key);
        int position = hash % nodes.length;
        Node<K, V> currentNode = nodes[position];
        if (currentNode == null) {
            nodes[position] = new Node<>(key, value, hash);
            size++;
        } else {
            while (currentNode.next != null) {
                if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(key, value, hash);
                size++;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
