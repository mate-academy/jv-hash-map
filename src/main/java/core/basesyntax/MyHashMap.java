package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[16];
        threshold = (int) (16 * 0.75f);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if (++size > threshold) {
            resize();
        }
        putNode(newNode);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> foundNode = getNode(key);
        return foundNode == null ? null : foundNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNode(Node<K, V> newNode) {
        int bucketIndex = getIndex(newNode.key);
        Node<K, V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = newNode;
            return;
        }
        Node<K, V> previousNode = null;
        while (currentNode != null) {
            if (Objects.equals(newNode.key, currentNode.key)) {
                currentNode.value = newNode.value;
                size--;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        previousNode.next = newNode;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        threshold = threshold << 1;
        Node<K, V>[] newTable = new Node[table.length << 1];
        transfer(newTable);
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        table = newTable;
        Node<K, V> current;
        for (Node<K, V> entry : oldTable) {
            current = entry;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
                size--;
            }
        }
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
