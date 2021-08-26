package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_LENGTH];
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        resize();
        int index = indexByHash(node.hash);
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }

        }
        table[index] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[indexByHash(nodeHash(key))];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void resize() {
        float threshold = table.length * LOAD_FACTOR;
        if (size == threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    Node<K, V> currentNode = node;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private int indexByHash(int hash) {
        return hash % table.length;
    }

    private int nodeHash(K key) {
        return key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
    }
}
