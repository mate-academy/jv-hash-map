package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        table = new Node[capacity];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = indexOfNode(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash(key), key, value,null);
            size++;
            resize();
            return;
        }
        while (currentNode != null) {
            if ((key == null && currentNode.key == null) || Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hash(key), key, value, null);
                size++;
                resize();
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[indexOfNode(key)];
        while (node != null) {
            if ((key == null && node.key == null) || Objects.equals(key,node.key)) {
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

    private void resize() {
        if (size >= threshold) {
            capacity *= 2;
            threshold = (int) (capacity * LOAD_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            for (Node<K, V> head: oldTable) {
                while (head != null) {
                    Node<K, V> nextNode = head.next;
                    int newIndex = indexOfNode(head.key);
                    head.next = table[newIndex];
                    table[newIndex] = head;
                    head = nextNode;
                }
            }
        }
    }

    private int hash(K key) {
        return (key == null ? 0 : key.hashCode());
    }

    private int indexOfNode(K key) {
        return Math.abs(hash(key)) % capacity;
    }
}
