package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Node<K, V>[] table;
    private int threshold;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> newNode = table[index];
            while (newNode != null) {
                if (Objects.equals(node.key, newNode.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = node;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
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

    private void resize() {
        if (size == threshold) {
            capacity = capacity * 2;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            for (Node<K, V> node : oldTable) {
                    while (node != null) {
                        put(node.key, node.value);
                        node = node.next;
                    }
                }
        }
    }

    private int hash(Object key) {
        return Objects.hash(key) % table.length;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(hash(key));
    }

    static class Node<K,V> {
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
