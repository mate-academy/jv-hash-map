package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (putToNode(key,value)) {
            size++;
            if (size > threshold) {
                table = resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hashKey = hash(key);
        Node<K, V> node = table[table.length - 1 & hashKey];
        while (node != null) {
            if (node.hash == hashKey && Objects.equals(node.key, key)) {
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

    private Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        threshold = threshold << 1;
        table = (Node<K,V>[])new Node[oldTab.length << 1];
        for (Node<K,V> node : oldTab) {
            while (node != null) {
                putToNode(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private boolean putToNode(K key, V value) {
        int keyHash = hash(key);
        Node<K, V> node = table[table.length - 1 & keyHash];
        if (node == null) {
            table[table.length - 1 & keyHash] = new Node<>(keyHash, key, value, null);
        }
        while (node != null) {
            if (node.hash == keyHash && Objects.equals(node.key,key)) {
                node.value = value;
                return false;
            }
            if (node.next == null) {
                node.next = new Node<>(keyHash, key,value, null);
                return true;
            }
            node = node.next;
        }
        return true;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
