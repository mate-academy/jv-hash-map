package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    public int hash(K key) {
        return (key == null) ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    public int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int bucketIndex = indexFor(hash, table.length);
        for (Node<K, V> currentNode = table[bucketIndex]; currentNode != null;
                currentNode = currentNode.next) {
            boolean hashMatches = currentNode.hash == hash;
            boolean keyMatches = (currentNode.key == key)
                    || (key != null && key.equals(currentNode.key));
            if (hashMatches && keyMatches) {
                currentNode.value = value;
                return;
            }
        }
        addNode(hash, key, value, bucketIndex);
    }

    public void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        if (++size >= threshold) {
            resize(2 * table.length);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int bucketIndex = indexFor(hash, table.length);
        for (Node<K, V> currentNode = table[bucketIndex]; currentNode != null;
                currentNode = currentNode.next) {
            boolean hashMatches = currentNode.hash == hash;
            boolean keyMatches = (currentNode.key == key)
                    || (key != null && key.equals(currentNode.key));
            if (hashMatches && keyMatches) {
                return currentNode.value;
            }
        }
        return null;
    }

    private void resize(int newCapacity) {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(node.hash, table.length);
                node.next = table[index];
                table[index] = node;
                node = next;
                size++;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
