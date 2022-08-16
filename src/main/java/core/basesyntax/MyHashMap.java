package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTORY = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTORY * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            increaseCapacity();
        }
        int index = getIndex(key);
        Node<K,V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (node.next != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        if (Objects.equals(node.key, key)) {
            node.value = value;
            return;
        }
        node.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void increaseCapacity() {
        int sizeArray = table.length * MAGNIFICATION_FACTOR;
        Node<K,V> [] oldTable = table;
        table = new Node[sizeArray];
        size = 0;
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key,node.value);
                node = node.next;
            }
        }
        threshold = (int) (sizeArray * LOAD_FACTORY);
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K,V> {
        private V value;
        private K key;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.value = value;
            this.key = key;
        }
    }
}
