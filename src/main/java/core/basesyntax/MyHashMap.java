package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] array;
    private int size;
    private int threshold;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndexByKey(key);
        Node<K, V> current = array[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(value, key, null);
                size++;
                return;
            }
            current = current.next;
        }
        array[index] = new Node<>(value, key, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> current = array[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newLength = array.length * 2;
        Node<K, V>[] oldArray = array;
        array = new Node[newLength];
        size = 0;
        Node<K, V> current;
        for (int i = 0; i < oldArray.length; i++) {
            current = oldArray[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        threshold = (int) (newLength * LOAD_FACTOR);
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % array.length;
    }

    private static class Node<K, V> {
        final K key;
        private V value;
        private Node<K, V> next;

        public Node(V value, K key, Node<K, V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
