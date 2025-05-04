package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K,V>[] table;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = Math.abs(hash % table.length);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> previousNode = null;
            while (currentNode != null) {
                if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = new Node<>(hash, key, value, null);
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key) % table.length);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int index = Math.abs(oldNode.hash % table.length);
                oldNode.next = table[index];
                table[index] = oldNode;
                oldNode = next;
            }
        }
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
