package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNecessary();
        int hash = hash(key);
        int index = getIndex(hash);
        Node<K, V> currentNode = table[index];
        Node<K, V> newNode = new Node<>(hash, key, value);
        if (currentNode == null) {
            table[index] = newNode;
        } else {
            Node<K, V> previous = null;
            while (currentNode != null) {
                if (areKeysEqual(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                previous = currentNode;
                currentNode = currentNode.next;
            }
            previous.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(hash(key));
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (areKeysEqual(currentNode.key, key)) {
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

    private int getIndex(int hash) {
        return hash % table.length;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void addToChain(Node<K,V> currentNode, Node<K,V> newNode) {
        Node<K, V> previous = null;
        while (currentNode != null) {
            previous = currentNode;
            currentNode = currentNode.next;
        }
        assert previous != null;
        newNode.next = null;
        previous.next = newNode;

    }

    private boolean areKeysEqual(K key, K newKey) {
        return key == newKey || key != null && key.equals(newKey);
    }

    private void resizeIfNecessary() {
        if (size == threshold) {
            table = resize();
        }
    }

    @SuppressWarnings({"unchecked"})
    private Node<K,V>[] resize() {
        Node<K,V>[] oldTable = table;
        int newCapacity = table.length << 1;
        table = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
        int index;
        Node<K, V> next;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                next = node.next;
                node.next = null;
                index = getIndex(node.hash);
                if (table[index] == null) {
                    table[index] = node;
                } else {
                    addToChain(table[index], node);
                }
                node = next;
            }
        }
        return table;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
