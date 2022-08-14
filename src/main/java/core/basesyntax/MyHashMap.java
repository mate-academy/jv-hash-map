package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private float threshold;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = hash(key);
        if (hashTable[index] == null) {
            hashTable[index] = newNode;
            size++;
            return;
        }
        Node<K, V> current = hashTable[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = newNode;
                size++;
            }
            current = current.next;
        }

    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = hashTable[hash(key)];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldTable = hashTable;
            hashTable = new Node[DEFAULT_CAPACITY * GROW_FACTOR];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : hashCode() % hashTable.length;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode() << 16)
                    & (value == null ? 0 : value.hashCode());
        }
    }
}
