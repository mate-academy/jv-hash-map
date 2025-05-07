package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int bucketIndex = indexFor(hash, table.length);
        if (size >= threshold) {
            resize(2 * table.length);
            bucketIndex = indexFor(hash, table.length);
        }
        for (Node<K, V> currentNode = table[bucketIndex]; currentNode != null;
                currentNode = currentNode.next) {
            if ((currentNode.key == key) || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
        }
        addNode(hash, key, value, bucketIndex);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int bucketIndex = indexFor(hash, table.length);
        for (Node<K, V> currentNode = table[bucketIndex]; currentNode != null;
                currentNode = currentNode.next) {
            if ((currentNode.key == key) || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
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

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
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
