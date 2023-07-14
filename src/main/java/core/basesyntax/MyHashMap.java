package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int STOCK_ARR_LENGTH = 16;
    private static final float MAX_FILL = 0.75f;
    private static final int GROWTH_COEFICIENT = 2;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[STOCK_ARR_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> tempNode = getNodeWith(key);
        if (tempNode == null) {
            addToEndOfBucket(key, value);
            size++;
            growTableIfNeeded();
        } else {
            tempNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> tempNode = getNodeWith(key);
        return tempNode != null ? tempNode.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucketFor(K key) {
        return Math.abs(hashOf(key) % table.length);
    }

    private Node<K,V> getNodeWith(K key) {
        Node<K,V> currNode = table[bucketFor(key)];
        if (currNode == null) {
            return null;
        }
        while (currNode != null) {
            if (currNode.hash == hashOf(key) && keysAreEqual(currNode.key, key)) {
                return currNode;
            }
            currNode = currNode.next;
        }
        return null;
    }

    private void growTableIfNeeded() {
        if (size < table.length * MAX_FILL) {
            return;
        }
        Node<K,V>[] oldArrCash = table;
        table = (Node<K, V>[])new Node[table.length * GROWTH_COEFICIENT];
        for (Node<K,V> node : oldArrCash) {
            while (node != null) {
                addToEndOfBucket(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void addToEndOfBucket(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currNode = table[bucketFor(newNode.key)];
        if (currNode == null) {
            table[bucketFor(key)] = newNode;
            return;
        }
        while (currNode.next != null) {
            currNode = currNode.next;
        }
        currNode.next = newNode;
    }

    private boolean keysAreEqual(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    private static int hashOf(Object key) {
        if (key == null) {
            return 0;
        } else {
            return key.hashCode();
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            hash = hashOf(key);
        }
    }
}
