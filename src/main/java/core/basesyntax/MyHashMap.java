package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int hashCode = hash(key);
        Node<K,V> node = table[getBucket(key)];
        Node<K,V> newNode = new Node<>(hashCode, key, value, null);
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
        table[getBucket(key)] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getBucket(key)];
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

    private void resize() {
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length * RESIZE_FACTOR];
        size = 0;
        for (Node<K,V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getBucket(K key) {
        return hash(key) % table.length;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}


