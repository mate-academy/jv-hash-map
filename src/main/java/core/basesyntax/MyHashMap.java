package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_COEFFICIENT = 0.75f;
    private static final int DEFAULT_RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] buckets;
    private int currentLoad;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
        currentLoad = (int) DEFAULT_LOAD_COEFFICIENT * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newElement = new Node<>(key, value, null);
        Node<K, V> element = buckets[index];
        if (element == null) {
            buckets[index] = newElement;
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
        Node<K, V> element = buckets[index];
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
        return (key == null)
               ? 0
               : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getIndex(K key) {
        return getHashCode(key) & buckets.length - 1;
    }

    private void resize() {
        if (currentLoad == size) {
            size = 0;
            Node<K, V>[] oldTable = buckets;
            buckets = new Node[buckets.length * DEFAULT_RESIZE_COEFFICIENT];
            currentLoad *= DEFAULT_RESIZE_COEFFICIENT;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
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
}
