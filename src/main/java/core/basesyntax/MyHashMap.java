package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_LENGTH = 16;
    private static final int LENGTH_MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] data;

    public MyHashMap() {
        data = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        if (size > data.length * LOAD_FACTOR) {
            resize();
        }
        int bucketIndex = (data.length - 1) & hash;
        if (data[bucketIndex] == null) {
            data[bucketIndex] = new Node<>(key, value, null);
        } else {
            Node<K, V> currentNode = data[bucketIndex];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> current = data[(data.length - 1) & hash];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        Node<K, V>[] oldData = data;
        data = new Node[data.length * LENGTH_MULTIPLIER];
        size = 0;
        for (Node<K, V> node : oldData) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
