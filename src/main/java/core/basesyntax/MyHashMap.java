package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> node = table[getIndexTable(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
        table[getIndexTable(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        V value = null;
        Node<K,V> current = table[getIndexTable(key)];
        while (current != null) {
            if (Objects.equals(current.key,key)) {
                value = current.value;
            }
            current = current.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndexTable(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldNodes = table;
        Node<K,V>[] newNodes = new Node[oldNodes.length * 2];
        table = newNodes;
        size = 0;
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (newNodes.length * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
