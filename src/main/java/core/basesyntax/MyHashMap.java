package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 1 << 4;
    static final float LOAD_FACTOR = 0.75f;

    private int capacity;
    private int threshold;
    private Node<K,V>[] bucketList;
    private int size;

    @Override
    public void put(K key, V value) {
        if (containsKey(key)) {
            Node<K,V> existingNode = getNode(hash(key), key);
            existingNode.setValue(value);
            return;
        }
        Node<K,V> newNode = new Node<>(hash(key), key, value, null);
        int newNodePos = hash(key) % capacity;
        if (bucketList[newNodePos] == null) {
            bucketList[newNodePos] = newNode;
            size++;
        } else {
            Node<K,V> existingNode = bucketList[newNodePos];
            while (existingNode.next != null) {
                existingNode = existingNode.next;
            }
            existingNode.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> valueNode = getNode(hash(key), key);
        return valueNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    /* ------------ Node inner class ------------ */

    private class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return "Node{" + key +
                    " =" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) &&
                    Objects.equals(value, node.value);
        }

        //TODO: rewrite hashCode
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    /* ------------ HashMap private methods ------------ */

    private boolean containsKey(K key) {
        //a
    }

    //TODO: optimise this method
    private Node<K,V> getNode(int hash, K key) {
        Node<K,V> searchedNode = bucketList[hash % capacity];
        while (searchedNode.getKey() != key || (key != null && !searchedNode.getKey().equals(key))) {
            searchedNode = searchedNode.next;
        }
        return searchedNode;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void put(Node<K,V> node) {
        put(node.getKey(), node.getValue());
    }

    private void resize() {
        if (size >= threshold) {
            int oldCapacity = capacity;
            int oldThreshold = threshold;
            capacity = capacity << 1;
            threshold = (int) (capacity * LOAD_FACTOR);
            Node<K,V>[] oldBucketList = bucketList;
//            Node<K,V>[] newBucketList = (Node<K,V>[]) new Node[newCapacity];
            bucketList = (Node<K,V>[]) new Node[capacity];
            Node<K,V> curNode;
            for (int i = 0; i < oldCapacity; i++) {
                curNode = oldBucketList[i];
                while(curNode != null) {
                    put(curNode);
                    curNode = curNode.next;
                }
            }
        }
    }
}
