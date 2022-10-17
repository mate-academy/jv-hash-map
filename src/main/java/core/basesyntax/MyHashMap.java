package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_COUNT = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * (int) DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> element = table[index];
        Node<K, V> newElement = new Node<>(getHashCode(key), key, value, null);
        if (element == null) {
            table[index] = newElement;
        }
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                element.value = value;
                return;
            }
            if (element.next == null) {
                element.next = newElement;
                break;
            }
            element = element.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = table[getIndex(key)];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashCode(K key) {
        int keysHash;
        return key == null ? 0 : (keysHash = key.hashCode()) ^ (keysHash >>> 16);
    }

    private int getIndex(K key) {
        return getHashCode(key) & table.length - 1;
    }

    private void resize() {
        if (size == threshold) {
            threshold *= DEFAULT_RESIZE_COUNT;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * DEFAULT_RESIZE_COUNT];
            transfer(oldTable);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
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
}
