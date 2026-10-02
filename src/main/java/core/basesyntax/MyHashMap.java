package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75d;
    private static final int DEFAULT_CAPACITY = 16;
    private int size = 0;
    private Node<K,V>[] table;
    private int threshold;
    
    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;
        
        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
    
    @Override
    public void put(K key, V value) {
        if (needResizing()) {
            resize();
        }
        
        if (key == null) {
            Node<K,V> currentNode = table[0];
            if (currentNode == null) {
                Node<K,V> newNode = new Node<>(0, null, value, null);
                table[0] = newNode;
                size++;
                return;
            } else {
                while (true) {
                    if (currentNode.key == null) {
                        currentNode.value = value;
                        return;
                    }
                    if (currentNode.next == null) {
                        Node<K,V> newNode = new Node<>(0, null, value, null);
                        currentNode.next = newNode;
                        size++;
                        return;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        
        int newNodeIndex = getIndex(key.hashCode(), table.length);
        Node<K,V> currentNode = table[newNodeIndex];
        int hash = key.hashCode();
        
        if (currentNode == null) {
            Node<K,V> newNode = new Node<>(hash, key, value, null);
            table[newNodeIndex] = newNode;
            size++;
            return;
        }
        
        while (true) {
            if (currentNode.hash == hash && key.equals(currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                Node<K,V> newNode = new Node<>(hash, key, value, null);
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }
    
    @Override
    public V getValue(K key) {
        if (table != null) {
            int hash = key == null ? 0 : key.hashCode();
            int index = getIndex(hash, table.length);
            
            Node<K,V> currentNode = table[index];
            
            while (currentNode != null) {
                if (currentNode.hash == hash && (Objects.equals(key, currentNode.key))) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
       
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
    
    private int getIndex(int hash, int capacity) {
        return (capacity - 1) & hash;
    }
    
    private void resize() {
        if (table == null) {
            table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
            threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
        }
        
        if (size >= threshold) {
            int newTableSize = table.length << 1;
            Node<K,V>[] oldTable = table;
            table = (Node<K,V>[]) new Node[newTableSize];
            threshold = (int) (LOAD_FACTOR * newTableSize);
            
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                   
                    int newNodeIndex = getIndex(node.hash, newTableSize);
                   
                    node.next = table[newNodeIndex];
                    table[newNodeIndex] = node;
                    node = nextNode;
                }
            }
        }
    }
    
    private boolean needResizing() {
        return table == null || size >= threshold;
    }
}
