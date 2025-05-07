package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        this.table = createTable(capacity);
    }

    @Override
    public void put(K key, V value) {
        resize();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node;
        return (node = getNode(key)) == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        if (table != null && size > 0) {
            size = 0;
            table = createTable(capacity);
        }
    }

    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    public boolean containsValue(Object value) {
        V tmpValue;
        if (table != null && size > 0) {
            for (Node<K, V> node : table) {
                for (; node != null; node = node.next) {
                    tmpValue = node.value;
                    if (tmpValue == value || value != null && value.equals(tmpValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void resize() {
        if (size >= threshold) {
            capacity = Math.min(2 * capacity, MAXIMUM_CAPACITY);
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            size = 0;
            Node<K, V>[] oldTable = table;
            table = createTable(capacity);
            for (Node<K, V> node : oldTable) {
                if (node == null) {
                    continue;
                }
                do {
                    putValue(node.key, node.value);
                } while ((node = node.next) != null);
            }
        }
    }

    private Node<K, V>[] createTable(final int tableCapacity) {
        return (Node<K, V>[]) new Node[tableCapacity];
    }

    private void putValue(K key, V value) {
        final int bucketIndex = getBucketIndex(key);
        if (table[bucketIndex] == null) { //create first node in bucket
            table[bucketIndex] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> node = table[bucketIndex];
        Node<K, V> prevNode = null;
        do { //update value in existing node (change node)
            if ((node.key == key || (key != null && key.equals(node.key)))) {
                if (prevNode == null) { // first node in bucket
                    table[bucketIndex] = new Node<>(key, value, node.next);
                    return;
                }
                prevNode.next = new Node<>(key, value, node.next);
                return;
            }
            prevNode = node;
        } while ((node = node.next) != null);
        //add new node at the end of list
        prevNode.next = new Node<>(key, value);
        size++;
    }

    public V remove(K key) {
        final int bucketIndex = getBucketIndex(key);
        Node<K, V> node = table[bucketIndex];
        if (node == null) {
            return null;
        }
        Node<K, V> prevNode = null;
        do {
            if ((node.key == key || (key != null && key.equals(node.key)))) {
                if (prevNode == null) { // first node in bucket
                    table[bucketIndex] = node.next;
                    size--;
                    return node.value;
                }
                prevNode.next = node.next;
                size--;
                return node.value;
            }
            prevNode = node;
        } while ((node = node.next) != null);
        return null;
    }

    private int getBucketIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private Node<K, V> getNode(Object key) {
        final int bucketIndex = getBucketIndex(key);
        Node<K, V> node = this.table[bucketIndex];
        if (node == null) {
            return null;
        }
        do {
            if (node.key == key || (key != null && key.equals(node.key))) {
                return node;
            }
        } while ((node = node.next) != null);
        return null;
    }

    private static final class Node<K, V> {
        private final K key;
        private Node<K, V> next;
        private V value;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node(K key, V value) {
            this(key, value, null);
        }

        public String toString() {
            return key + "=" + value;
        }
    }
}
