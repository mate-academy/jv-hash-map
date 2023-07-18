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
        resize();
        Node<K,V> newNode = new Node<>(hash(key), key, value, null);
        //if (putNode(newNode)) {
        //    size++;
        //}

        int newNodePos = getBucketPos(hash(key));
        if (bucketList[newNodePos] == null) {
            bucketList[newNodePos] = newNode;
        } else {
            Node<K,V> existingNode = bucketList[newNodePos];
            while (existingNode.next != null) {
                existingNode = existingNode.next;
            }
            existingNode.next = newNode;
        }
        size++;
    }

    private void put(Node<K,V> node) {
        put(node.getKey(), node.getValue());
    }

    @Override
    public V getValue(K key) {
        Node<K,V> valueNode = getNode(hash(key), key);
        return valueNode == null ? null : valueNode.getValue();
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
            return "Node{" + key
                    + " =" + value
                    + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key)
                    && Objects.equals(value, node.value);
        }

        //TODO: rewrite hashCode
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    /* ------------ HashMap private methods ------------ */

    private boolean containsKey(K key) {
        if (getNode(hash(key), key) != null) {
            return true;
        }
        return false;
    }

    private Node<K,V> getNode(int hash, K key) {
        Node<K,V> searchedNode = null;
        if (capacity != 0 && bucketList[getBucketPos(hash)] != null) {
            searchedNode = bucketList[getBucketPos(hash)];

            while (!compareNodeHashAndKey(searchedNode, hash, key)) {
                if ((searchedNode = searchedNode.next) == null) {
                    break;
                }
            }
        }
        return searchedNode;
    }

    private boolean compareNodeHashAndKey(Node<K,V> node, int hash, K key) {
        if (node.hash == hash && (node.key == key
                || (node.key != null && node.key.equals(key)))) {
            return true;
        }
        return false;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucketPos(int hash) {
        return (hash < 0 ? hash * -1 : hash) % capacity;
    }

    private boolean putNode(Node<K,V> node) {
        if (containsKey(node.key)) {
            Node<K,V> existingNode = getNode(node.hash, node.key);
            existingNode.setValue(node.value);
            return false;
        }
        //resize();
        int newNodePos = getBucketPos(node.hash);
        if (bucketList[newNodePos] == null) {
            bucketList[newNodePos] = node;
        } else {
            Node<K,V> existingNode = bucketList[newNodePos];
            while (existingNode.next != null) {
                existingNode = existingNode.next;
            }
            existingNode.next = node;
        }
        return true;
    }

    private void resize() {
        if (capacity == 0) {
            capacity = DEFAULT_CAPACITY;
            threshold = (int) (capacity * LOAD_FACTOR);
            bucketList = (Node<K,V>[]) new Node[capacity];
        }
        if (size >= threshold && threshold > 0) {
            capacity = capacity << 1;
            threshold = (int) (capacity * LOAD_FACTOR);
            int oldCapacity = capacity;
            Node<K,V>[] oldBucketList = bucketList;
            bucketList = (Node<K,V>[]) new Node[capacity];
            Node<K,V> curNode;
            for (int i = 0; i < oldCapacity; i++) {
                curNode = oldBucketList[i];
                while (curNode != null) {
                    //putNode(curNode);
                    put(curNode);
                    size--;
                    curNode = curNode.next;
                }
            }
        }
    }
}
