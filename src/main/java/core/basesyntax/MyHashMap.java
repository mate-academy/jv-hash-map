package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int defCapacity = 16;
    private static final double loadFactor = 0.75;
    private Node<K, V>[] table = new Node[defCapacity];
    private int capacity = defCapacity;
    private int size = 0;
    private int threshold = (int) (capacity * loadFactor);

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public int getHash() {
            return hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> node = new Node<>();
        node.setHash(key == null ? 0 : key.hashCode());
        node.setKey(key);
        node.setValue(value);

        Node<K, V> curNode = table[getBucketIndex(node.getKey())];
        if (curNode == null) {
            table[getBucketIndex(node.getKey())] = node;
            size++;
        } else {
            while (curNode.getNext() != null && !keysAreEquals(curNode.getKey(), key)) {
                curNode = curNode.getNext();
            }
            if (keysAreEquals(curNode.getKey(), key)) {
                curNode.setValue(value);
            } else {
                curNode.setNext(node);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0 || size == 0) {
            return null;
        }
        Node<K, V> curNode = table[key == null ? 0 : getBucketIndex(key)];
        if (curNode == null) {
            return null;
        }
        while (curNode.getNext() != null && !keysAreEquals(curNode.getKey(), key)) {
            curNode = curNode.getNext();
        }
        return curNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private boolean keysAreEquals(K key1, K key2) {
        return key1 == key2 || (key1 != null && key1.equals(key2));
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }

    }
}
