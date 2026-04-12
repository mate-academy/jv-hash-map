package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final int HASH_MASK = 0x7fffffff;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
   
    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }
    
    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash
                    && (current.key == key
                    || (key != null && key.equals(current.key)))) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[index] = new Node<>(hash, key, value, table[index]);
        size++;
        if (size > threshold) {
            resize();
        }
    }
   
    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash
                    && (current.key == key
                    || (key != null && key.equals(current.key)))) {
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
        int newCapacity = table.length * RESIZE_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = node.hash % newCapacity;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }
    
    private int hash(K key) {
        return key == null ? 0 : (key.hashCode() & HASH_MASK);
    }
   
    private int indexFor(int hash) {
        return hash % table.length;
    }
    
    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;
        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
