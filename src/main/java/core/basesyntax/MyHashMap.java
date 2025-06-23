package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    
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
    
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }
    
    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        
        while (current != null) {
            if (keyEquals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        
        while (current != null) {
            if (keyEquals(current.key, key)) {
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
    
    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }
    
    private boolean keyEquals(K k1, K k2) {
        return (k1 == k2) || (k1 != null && k1.equals(k2));
    }
    
    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        
        for (Node<K, V> head : table) {
            Node<K, V> node = head;
            
            while (node != null) {
                Node<K, V> next = node.next;
                
                int newIndex = (node.key == null ? 0 : Math.abs(node.key.hashCode()) % newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        
        table = newTable;
        threshold = (int)(newCapacity * DEFAULT_LOAD_FACTOR);
    }
}
