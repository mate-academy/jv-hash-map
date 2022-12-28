package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node node = new Node(key, value);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        }
        for (Node<K, V> newNode = table[index]; newNode != null; newNode = newNode.next) {
            if (Objects.equals(newNode.key, node.key)) {
                newNode.value = (V) node.value;
                break;
            }
            if (newNode.next == null) {
                newNode.next = node;
                size++;
                break;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void resize() {
        int newLength = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        size = 0;
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
