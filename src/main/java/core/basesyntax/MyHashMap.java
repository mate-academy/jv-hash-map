package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private int defCapacity = 16;
    private int capacity;
    private int size = 0;
    private double loadFactor = 0.75;
    private int threshold = 0;

    private void resize() {
        if (table == null || capacity == 0) {
            capacity = defCapacity;
            table = new Node[defCapacity];
            threshold = (int) (capacity * loadFactor);
        } else {
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

    @Override
    public void put(K key, V value) {
        if (capacity == 0 || size + 1 > threshold) {
            resize();
        }
        Node<K, V> node = new Node<>();
        node.setHash(key == null ? 0 : key.hashCode());
        node.setKey(key);
        node.setValue(value);

        Node<K, V> curNode = table[getBucketIndex(node.getHash())];
        if (curNode == null) {
            table[Math.abs(node.getHash()) % capacity] = node;
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

    private int getBucketIndex(int hash) {
        return Math.abs(hash) % capacity;
    }

    private boolean keysAreEquals(K key1, K key2) {
        return key1 == key2 || (key1 != null && key1.equals(key2));
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0 || size == 0) {
            return null;
        }
        Node<K, V> curNode = table[key == null ? 0 : getBucketIndex(key.hashCode())];
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
}
