package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> targetNode = getNode(key);
        if (targetNode != null) {
            targetNode.value = value;
            return;
        }
        if (size == threshold) {
            resize();
        }
        addNode(getHash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> targetNode = getNode(key);
        return targetNode == null ? null : targetNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public V remove(K key) {
        int hash = getHash(key);
        int index = hash % table.length;
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        }
        if (node.hash == hash && (node.key == key
                || (node.key != null && node.key.equals(key)))) {
            V oldValue = node.value;
            table[index] = null;
            size--;
            return oldValue;
        }
        Node<K, V> prevNode = node;
        node = node.next;
        while (node != null) {
            if (node.hash == hash && (node.key == key
                    || (node.key != null && node.key.equals(key)))) {
                prevNode.next = node.next;
                V oldValue = node.value;
                node = null;
                size--;
                return oldValue;
            }
            prevNode = prevNode.next;
            node = node.next;
        }
        return null;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private Node<K,V> getNode(K key) {
        int hash = getHash(key);
        Node<K, V> node = table[hash % table.length];
        while (node != null) {
            if (node.hash == hash && (node.key == key
                    || (node.key != null && node.key.equals(key)))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addNode(int hash, K key, V value) {
        int index = hash % table.length;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
    }

    private void resize() {
        int newCapacity = table.length << 1;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[])new Node[newCapacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                addNode(node.hash, node.key, node.value);
                node = node.next;
            }
        }
    }
}
