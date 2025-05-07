package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;
    
    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[INITIAL_CAPACITY];
    }
    
    @Override
    public void put(K key, V value) {
        int hash;
        int index = indexNode(key);
        Node<K, V> node;
        node = table[index];
        hash = hash(key);
        
        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return;
        }
        while (node.next != null || Objects.equals(node.key, key)) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node.next = new Node<>(hash, key, value, node.next);
        size++;
        if (size >= threshold) {
            resize();
        }
    }
    
    @Override
    public V getValue(K key) {
        Node<K, V> node = table[indexNode(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return (V) node.value;
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
        Node<K, V>[] oldTable;
        int capacity = table.length * RESIZE;
        threshold = threshold * RESIZE;
        oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
    
    private int hash(K key) {
        int result = 17;
        result = 31 + result + (key != null ? key.hashCode() : 0);
        return Math.abs(result);
    }
    
    private int indexNode(K key) {
        return (table.length - 1) & hash(key);
    }
    
    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;
        
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
