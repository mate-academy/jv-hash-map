package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    
    private Node<K, V>[] table;
    private int threshold;
    private int size;
    
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }
    
    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }
    
    @Override
    public void put(K key, V value) {
        int hash = getIndex(key);
        Node<K, V> currentNode = table[hash];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (currentNode == null) {
            table[hash] = newNode;
        } else {
            while (true) {
                if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        size = 0;
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
    
    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
    
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        
        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
