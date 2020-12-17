package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size;
    private Node<K, V>[] nodesArray;

    public MyHashMap() {
        nodesArray = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = setHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (nodesArray[hash] == null) {
            nodesArray[hash] = newNode;
            checkSize(size++);
            return;
        }
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
            checkSize(size++);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = setHash(key);
        if (nodesArray != null && nodesArray[hash] != null) {
            Node<K, V> node = nodesArray[hash];
            while (node.next != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize(int size) {
        if (size > nodesArray.length * LOAD_FACTOR) {
            resize();
        } else {
            size++;
        }
    }

    private int setHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % nodesArray.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] node = nodesArray;
        nodesArray = (Node<K, V>[]) new Node[node.length * 2];
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
