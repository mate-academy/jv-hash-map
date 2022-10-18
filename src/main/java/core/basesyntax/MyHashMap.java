package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREASE = 2;
    private Node<K, V>[] table;
    private int size;
    private int defaultLoad;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.defaultLoad = (int) LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        int index = getIndex(key);
        Node<K, V> newElement = new Node<>(hash, value, key, null);
        Node<K, V> element = table[index];
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
        int index = getIndex(key);
        Node<K, V> element = table[index];
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

    private int getHash(K key) {
        int hash;
        return key == null ? 0 : (hash = key.hashCode()) ^ hash >>> 16;
    }

    private int getIndex(K key) {
        return getHash(key) & table.length - 1;
    }

    private void resize() {
        if (defaultLoad == size) {
            size = 0;
            Node<K, V>[] currentTable = table;
            table = new Node[table.length * DEFAULT_INCREASE];
            defaultLoad *= DEFAULT_INCREASE;
            for (Node<K, V> node : currentTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private V value;
        private final K key;
        private Node<K, V> next;

        public Node(int hash, V value, K key, Node<K, V> next) {
            this.hash = hash;
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
