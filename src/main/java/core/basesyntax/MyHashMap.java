package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int STOCK_ARRAY_LENGTH = 16;
    private static final float MAX_FILL = 0.75f;
    private static final int GROWTH_COEFICIENT = 2;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[STOCK_ARRAY_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> tempNode = getNode(key);
        if (tempNode == null) {
            growTableIfNeeded();
            addNewNode(key, value);
            size++;
        } else {
            tempNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> tempNode = getNode(key);
        return tempNode != null ? tempNode.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucket(K key) {
        return Math.abs(hashOf(key) % table.length);
    }

    private Node<K,V> getNode(K key) {
        Node<K,V> currentNode = table[getBucket(key)];
        while (currentNode != null) {
            if (currentNode.hash == hashOf(key) && keysAreEqual(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void growTableIfNeeded() {
        if (size < table.length * MAX_FILL) {
            return;
        }
        Node<K,V>[] oldTable = table;
        table = (Node<K, V>[])new Node[table.length * GROWTH_COEFICIENT];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                addNewNode(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void addNewNode(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = table[getBucket(newNode.key)];
        if (currentNode == null) {
            table[getBucket(key)] = newNode;
            return;
        }
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
    }

    private boolean keysAreEqual(K first, K second) {
        return first == second || first != null && first.equals(second);
    }

    private static int hashOf(Object key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    private class Node<K, V> {
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
