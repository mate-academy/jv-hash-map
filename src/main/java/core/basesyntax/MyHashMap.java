package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] hashTable;
    private int size;
    private int threshold;

    public MyHashMap() {
        hashTable = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putIfKeyIsNull(value);
            return;
        }
        int hash = hashCode(key);
        int index = indexFor(hash, hashTable.length);
        Node<K, V> node = findNodeByKey(key, hash, index);
        if (node != null) {
            node.value = value;
        } else {
            addNode(hash, key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int hash = hashCode(key);
        int index = indexFor(hash, hashTable.length);
        Node<K, V> node = findNodeByKey(key, hash, index);
        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putIfKeyIsNull(V value) {
        Node<K, V> node = hashTable[0];
        while (node != null) {
            if (node.key == null) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(0, null, value, 0);
    }

    private void addNode(int hash, K key, V value, int bucketIndex) {
        if (size >= threshold) {
            resize(RESIZE_FACTOR * hashTable.length);
            hash = hashCode(key);
            bucketIndex = indexFor(hash, hashTable.length);
        }
        Node<K, V> newNode = new Node<>(hash, key, value, hashTable[bucketIndex]);
        hashTable[bucketIndex] = newNode;
        size++;
    }

    private V getForNullKey() {
        for (Node<K, V> node = hashTable[0]; node != null; node = node.next) {
            if (node.key == null) {
                return node.value;
            }
        }
        return null;
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        transfer(newTable);
        hashTable = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : hashTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(node.hash, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    public int hashCode(K key) {
        return 31 * 17 + key.hashCode();
    }

    private Node<K, V> findNodeByKey(K key, int hash, int index) {
        for (Node<K, V> node = hashTable[index]; node != null; node = node.next) {
            if (node.hash == hash && key.equals(node.key)) {
                return node;
            }
        }
        return null;
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
