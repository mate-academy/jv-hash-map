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
        size = 0;
    }
    
    @Override
    public void put(K key, V value) {
        int hash;
        int i;
        Node<K, V> p;
        p = table[i = indexNode(key)];
        hash = hash(key);
        
        if (p == null) {
            table[i] = new Node<>(hash, key, value, null);
            size++;
            return;
        }
        while (p.next != null || Objects.equals(p.key, key)) {
            if (Objects.equals(p.key, key)) {
                p.value = value;
                return;
            }
            p = p.next;
        }
        p.next = new Node<>(hash, key, value, p.next);
        size++;
        if (size >= threshold) {
            resize();
        }
    }
    
    @Override
    public V getValue(K key) {
        Node<K, V> p = table[indexNode(key)];
        while (p != null) {
            if (Objects.equals(key, p.key)) {
                return (V) p.value;
            }
            p = p.next;
        }
        return null;
    }
    
    @Override
    public int getSize() {
        return size;
    }
    
    public void resize() {
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
    
    public int hash(K key) {
        int result = 17;
        result = 31 + result + (key != null ? key.hashCode() : 0);
        return Math.abs(result);
    }
    
    public int indexNode(K key) {
        return (table.length - 1) & hash(key);
    }
    
    static class Node<K, V> {
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
