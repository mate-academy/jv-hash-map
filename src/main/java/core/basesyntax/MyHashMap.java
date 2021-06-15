package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;
    private int size;
    private int treshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        treshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        if (size >= treshold) {
            resize();
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[size * MAGNIFICATION_FACTOR];
        size = 0;
        for (Node<K, V> kvNode : oldTable) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
        treshold *= MAGNIFICATION_FACTOR;
    }

    private int calculateIndex(K key) {
        return (key == null) ? 0 : hash(key) % table.length;
    }

    private int hash(K key) {
        return key == null ? 0 : (key.hashCode() ^ (key.hashCode() >>> 16));
    }
}
