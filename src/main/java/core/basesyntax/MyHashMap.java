package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_LENGTH = 16;
    private int size;
    private Node<K, V>[] data;
    private int threshold;

    @Override
    public void put(K key, V value) {
        Node<K, V>[] arrayOfData = data;
        int hash = hash(key);
        if (arrayOfData == null || size >= threshold) {
            resize();
            arrayOfData = data;
        }
        if (arrayOfData[(arrayOfData.length - 1) & hash] == null) {
            arrayOfData[(arrayOfData.length - 1) & hash] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> currentNode = arrayOfData[(arrayOfData.length - 1) & hash];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash, key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
        data = arrayOfData;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V>[] ourData = data;
        if (ourData == null || ourData[(ourData.length - 1) & hash] == null) {
            return null;
        }
        Node<K, V> current = ourData[(ourData.length - 1) & hash];
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
        int oldLength = oldData == null ? 0 : oldData.length;
        int oldThreshold = threshold;
        int newLength = 0;
        int newThreshold = 0;
        if (oldThreshold == 0) {
            newLength = DEFAULT_LENGTH;
            newThreshold = (int)(DEFAULT_LENGTH * LOAD_FACTOR);
        }
        if (size > oldThreshold) {
            newLength = oldLength << 1;
            newThreshold = (int)(newLength * LOAD_FACTOR);
        }
        Node<K, V>[] newData = (Node<K, V>[]) new Node[newLength];
        threshold = newThreshold;
        data = newData;
        if (oldData != null) {
            size = 0;
            for (Node<K, V> node : oldData) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
