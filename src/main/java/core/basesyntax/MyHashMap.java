package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final int INCREASE_CAPACITY = 2;
    private Node<K, V>[] table;
    private int size;
    private int threhold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threhold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threhold) {
            resize();
        }
        int bucket = hash(key);
        if (table[bucket] == null) {
            table[bucket] = new Node(key, value, null);
            size++;
        } else {
            Node<K, V> node = table[bucket];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node(key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = hash(key);
        Node<K, V> node = table[bucket];
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

    public Node<K, V>[] resize() {
        threhold = (int) (table.length * INCREASE_CAPACITY * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * INCREASE_CAPACITY];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
