package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold = 0;
    private int initialCapacity = 0;

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        initialCapacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int hash = getHash(key);
        
        if (size > threshold) {
            resize();
        }

        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> searchNode = table[hash];

            while (searchNode != null) {
                if (equals(searchNode.key, key)) {
                    searchNode.value = value;
                    searchNode = null;
                } else if (searchNode.next == null) {
                    searchNode.next = new Node<>(hash, key, value, null);
                    searchNode = null;
                    size++;
                } else {
                    searchNode = searchNode.next;
                }
            }
        }
    }

    private void resize() {
        int newInitialCapacity = table.length * 2;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newInitialCapacity);
        Node<K, V>[] oldTable = table; 
        table = new Node[newInitialCapacity];
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K,V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];

            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        Node<K, V> searchNode = table[hash];

        while (searchNode != null) {
            if (equals(searchNode.key, key)) {
                return searchNode.value;
            }
            searchNode = searchNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return Math.abs((key == null) ? 0 : (key.hashCode() % initialCapacity));
    }

    private boolean equals(K firstKey, K secondKey) {
        return firstKey == secondKey || (secondKey != null && secondKey.equals(firstKey));
    }
}
