package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            initialMyHash();
        }
        if (size >= threshold) {
            resize();
        }
        int currentKeyHash = hash(key);
        int i = getIndex(currentKeyHash);
        Node<K, V> newNode = new Node<>(currentKeyHash, key, value, null);
        if ((table[i]) == null) {
            table[i] = newNode;
            size++;
            return;
        }
        putValueInTree(table[i], newNode);
    }

    @Override
    public V getValue(K key) {
        if ((size == 0)) {
            return null;
        }
        int currentKeyHash = hash(key);
        int i = getIndex(currentKeyHash);
        Node<K, V> currentNode;
        if ((currentNode = table[i]) != null) {
            do {
                if ((currentNode.hash == currentKeyHash) && ((currentNode.key == key)
                        || ((key != null) && key.equals(currentNode.key)))) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValueInTree(Node<K, V> rootNode, Node<K, V> growNode) {
        if ((rootNode.hash == growNode.hash) && ((growNode.key == rootNode.key)
                || ((growNode.key != null) && growNode.key.equals(rootNode.key)))) {
            rootNode.value = growNode.value;
            return;
        }
        if (rootNode.next == null) {
            rootNode.next = growNode;
            size++;
            return;
        }
        rootNode = rootNode.next;
        putValueInTree(rootNode, growNode);
    }

    private void initialMyHash() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private void resize() {
        int newSize;
        if ((newSize = table.length << 1) <= MAXIMUM_CAPACITY) {
            threshold = (int) (newSize * DEFAULT_LOAD_FACTOR);
            Node<K, V>[] tempTable = table;
            table = (Node<K, V>[]) new Node[newSize];
            size = 0;
            for (Node<K, V> e : tempTable) {
                for (; e != null; e = e.next) {
                    put(e.key, e.value);
                }
            }
        }
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int getIndex(int hash) {
        return ((table.length - 1) & hash);
    }

    private static class Node<K, V> {
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
