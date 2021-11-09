package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_CAPACITY = 16;
    private static float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] nodes;
    private int size;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= nodes.length * LOAD_FACTOR) {
            grow();
        }
        Node<K, V> newNode = nodes[getKey(key)];
        if (newNode == null) {
            nodes[getKey(key)] = new Node<>(key, value, null);
        }
        while (newNode != null) {
            if (Objects.equals(newNode.key,key)) {
                newNode.value = value;
                return;
            } else if (newNode.next == null) {
                Node<K, V> thisNode = new Node<>(key, value, null);
                newNode.next = thisNode;
                break;
            }
            newNode = newNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodes[getKey(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        size = 0;
        Node<K,V>[] oldNode = nodes;
        nodes = new Node[nodes.length * 2];
        for (Node<K, V> newNode : oldNode) {
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private int getKey(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % nodes.length);
    }
}
