package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static int capacity = 1;
    private int size = 0;
    private Node<K, V>[] nodesArray;

    public MyHashMap() {
        nodesArray = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        capacity = nodesArray.length;
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            resize();
        }
        int hash = setHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (nodesArray[hash] != null) {
            Node<K, V> node = nodesArray[hash];
            while (node.next != null) {
                if (Objects.equals(node.key,key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (Objects.equals(node.key, key)) {
                node.value = newNode.value;
                return;
            }
            node.next = newNode;
            size++;
        }
        if (nodesArray[hash] == null) {
            nodesArray[hash] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = setHash(key);
        if (nodesArray == null || nodesArray[hash] == null) {
            return null;
        }
        Node<K, V> node = nodesArray[hash];
        while (node.next != null) {
            if (Objects.equals(node.key,key)) {
                return node.value;
            }
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int setHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] node = nodesArray;
        nodesArray = (Node<K, V>[]) new Node[capacity * 2];
        capacity = nodesArray.length;
        for (Node<K, V> nodes : node) {
            while (nodes != null) {
                put(nodes.key, nodes.value);
                nodes = nodes.next;
            }
        }
    }

    private static class Node<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
