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
        int pos = bucketFor(key);
        if (table[pos] == null) {
            size++;
            growTableIfNeeded();
            table[pos] = new Node<>(key, value);
            return;
        }
        Node<K,V> tempNode = closestNode(key);
        if (keysAreEqual(key, tempNode.key)) {
            tempNode.value = value;
            return;
        }
        tempNode.next = new Node<>(key, value);
        size++;
        growTableIfNeeded();
    }

    @Override
    public V getValue(K key) {
        Node<K,V> tempNode = closestNode(key);
        return tempNode != null
                && tempNode.hash == hashOf(key)
                && (keysAreEqual(tempNode.key, key))
                ? tempNode.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucketFor(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K,V> closestNode(K key) {
        Node<K,V> currNode = table[bucketFor(key)];
        if (currNode == null) {
            return null;
        }
        while (currNode.next != null) {
            if (currNode.hash == hashOf(key) && keysAreEqual(currNode.key, key)) {
                return currNode;
            }
            currNode = currNode.next;
        }
        return currNode;
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
        Node<K, V> node = new Node<>(key, value);
        Node<K, V> currNode = table[bucketFor(node.key)];
        if (currNode == null) {
            table[bucketFor(key)] = node;
            return;
        }
        while (currNode.next != null) {
            currNode = currNode.next;
        }
        currNode.next = node;
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
