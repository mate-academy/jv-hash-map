package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE_FACTOR = 2;
    private Node<K,V>[] table;
    private int currentLoad;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        currentLoad = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHashCode(key);
        int index = getIndex(key);
        Node<K, V> newElement = new Node<>(key, value, null);
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
        int hash = getHashCode(key);
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

    private int getHashCode(K key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getIndex(K key) {
        return getHashCode(key) & table.length - 1;
    }

    private void resize() {
        if (currentLoad == size) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * DEFAULT_RESIZE_FACTOR];
            currentLoad *= DEFAULT_RESIZE_FACTOR;
            for (Node<K,V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
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
