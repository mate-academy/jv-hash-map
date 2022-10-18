package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREASE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    public class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key) & (table.length - 1);
        Node<K, V> newNode = table[index];
        if (newNode == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        }
        while (newNode != null) {
            if (newNode.hash == hash(key) && (Objects.equals(key, newNode.key))) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = new Node<>(hash(key), key, value, null);
                break;
            }
            newNode = newNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) & (table.length - 1);
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (newNode.hash == hash(key) && (Objects.equals(key, newNode.key))) {
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

    public int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public void resize() {
        Node<K, V>[] oldTab = table;
        if (size == threshold) {
            size = 0;
            table = (Node<K, V>[]) new Node[table.length * DEFAULT_INCREASE];
            threshold = threshold * DEFAULT_INCREASE;
            for (Node<K, V> kvNode : oldTab) {
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }
}
