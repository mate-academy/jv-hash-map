package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 1;
    private int capacity = 16;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        resizeIfNecessary();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int remainder = Math.abs(getHash(key)) % capacity;
        if (size == 0 || table[remainder] == null) {
            return null;
        }
        Node<K, V> tempNode = table[remainder];
        while (tempNode != null) {
            if (isEquals(tempNode, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNecessary() {
        if (threshold == 0) {
            threshold = (int) (LOAD_FACTOR * capacity);
            table = new Node[capacity];
            return;
        }
        if (threshold == size) {
            Node<K, V>[] tempArr = table;
            capacity = capacity << INCREASE_FACTOR;
            transformArray(tempArr);
            threshold = (int) (LOAD_FACTOR * capacity);
        }
    }

    private void putValue(K key, V value) {
        int remainder = Math.abs(getHash(key)) % capacity;
        if (table[remainder] == null) {
            table[remainder] = new Node<>(getHash(key), key, value, null);
            size++;
            return;
        }
        iterateBucketAndPut(table[remainder], key, value);
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void iterateBucketAndPut(Node<K, V> node, K key, V value) {
        Node<K, V> tempNode = node;
        while (tempNode != null) {
            if (isEquals(tempNode, key)) {
                tempNode.value = value;
                return;
            }
            if (tempNode.next == null) {
                tempNode.next = new Node<>(getHash(key), key, value, null);
                size++;
                return;
            }
            tempNode = tempNode.next;
        }
    }

    private boolean isEquals(Node<K, V> temp, K key) {
        return (temp.key == key)
                || (temp.key != null && temp.key.hashCode() == getHash(key)
                && temp.key.equals(key));
    }

    private void transformArray(Node<K, V>[] tempArr) {
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : tempArr) {
            if (node != null) {
                Node<K, V> tempNode = node;
                while (tempNode != null) {
                    putValue(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
