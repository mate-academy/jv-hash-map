package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;

    private int capacity;
    private Node<K,V>[] bucketList;
    private int size;

    @Override
    public void put(K key, V value) {
        resize();
        if (putNode(key, value)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> valueNode = getNode(hash(key), key);
        return valueNode == null ? null : valueNode.value;
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

        @Override
        public String toString() {
            return "Node{" + key
                    + " =" + value
                    + '}';
        }
    }

    /* ------------ HashMap private methods ------------ */

    private boolean containsKey(K key) {
        return getNode(hash(key), key) != null;
    }

    private Node<K,V> getNode(int hash, K key) {
        Node<K,V> searchedNode = null;
        if (capacity != 0 && bucketList[getBucketPos(hash)] != null) {
            searchedNode = bucketList[getBucketPos(hash)];

            while (!(searchedNode.hash == hash && (searchedNode.key == key
                    || (searchedNode.key != null && searchedNode.key.equals(key))))) {
                if ((searchedNode = searchedNode.next) == null) {
                    break;
                }
            }
        }
        return searchedNode;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getBucketPos(int hash) {
        return (hash < 0 ? hash * -1 : hash) % capacity;
    }

    private boolean putNode(K key, V value) {
        Node<K,V> node = new Node<>(hash(key), key, value, null);
        if (containsKey(node.key)) {
            Node<K,V> existingNode = getNode(node.hash, node.key);
            existingNode.value = node.value;
            return false;
        }
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
            bucketList = (Node<K,V>[]) new Node[capacity];
        }
        if (size >= (capacity * LOAD_FACTOR)) {
            final int oldCapacity = capacity;
            capacity = capacity << 1;
            Node<K,V> curNode;
            Node<K,V>[] oldBucketList = bucketList;
            bucketList = (Node<K,V>[]) new Node[capacity];
            for (int i = 0; i < oldCapacity; i++) {
                curNode = oldBucketList[i];
                while (curNode != null) {
                    putNode(curNode.key, curNode.value);
                    curNode = curNode.next;
                }
            }
        }
    }
}
