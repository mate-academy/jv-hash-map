package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        if (putVal(key, value) == null) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int calculateIndex(int hashCode) {
        return Math.abs(hashCode % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        threshold = threshold << 1;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                node.next = null;
                relocateNode(node);
                node = next;
            }
        }
    }

    private void relocateNode(Node<K, V> node) {
        int index = calculateIndex(node.hash);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = node;
            return;
        }
        while (current.next != null) {
            current = current.next;
        }
        current.next = node;
    }

    private V putVal(K key, V value) {
        int hash = hash(key);
        int index = calculateIndex(hash);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value);
            return null;
        }
        Node<K, V> previousNode;
        do {
            if (hash == currentNode.hash && (keyIsEqual(key, currentNode.key))) {
                V oldVal = currentNode.value;
                currentNode.value = value;
                return oldVal;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        } while (currentNode != null);
        previousNode.next = new Node<>(hash, key, value);
        return null;
    }

    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = calculateIndex(hash);
        Node<K, V> node = table[index];
        while (node != null) {
            if (hash == node.hash && (keyIsEqual(key, node.key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private boolean keyIsEqual(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }
}
