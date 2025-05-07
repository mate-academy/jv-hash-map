package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growIfNeed();
        int index = getIndex(key);
        if (buckets[index] == null) {
            addNewNode(key, value, index);
        } else {
            Node<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value);
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> current = buckets[index];
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

    private void addNewNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value);
        buckets[index] = newNode;
        size++;
    }

    private int getIndex(K key) {
        int index = key == null ? 0 : key.hashCode();
        return index == 0 ? 0 : Math.abs(index % buckets.length);
    }

    private void growIfNeed() {
        if (size == buckets.length * LOAD_FACTOR) {
            resizeAndRehash();
        }
    }

    private void resizeAndRehash() {
        Node<K, V>[] currentTable = buckets;
        buckets = (Node<K, V>[]) new Node[currentTable.length * RESIZE_FACTOR];
        size = 0;
        for (Node<K, V> node : currentTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
