package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private float loadfactor;
    private int treshold;
    private int capacity;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        loadfactor = DEFAULT_LOAD_FACTOR;
        treshold = (int) (loadfactor * capacity);
    }

    @Override
    public void put(K key, V value) {
        sizeCheck();
        int hash = hashKey(key);
        int bucket = 0;
        if (key != null) {
            bucket = hash % capacity;
        }
        if (checkBucket(bucket, hash, key, value)) {
            Node<K, V> newNode = new Node<>(hash, key, value, null);
            if (table[bucket] == null) {
                table[bucket] = newNode;
                size++;
            } else {
                addInBucket(newNode, bucket);
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hashKey(key);
        int bucket = 0;
        if (key != null) {
            bucket = hash % capacity;
        }
        if (table[bucket] == null) {
            return null;
        }
        Node<K, V> node = table[bucket];
        do {
            if (node.hash == hash && (node.key == key || key != null
                    && node.key != null && node.key.equals(key))) {
                return node.value;
            }
            node = node.next;
        } while (node != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void sizeCheck() {
        if (size > treshold) {
            resize();
        }
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        capacity = capacity << 1;
        treshold = (int) (capacity * loadfactor);
        Node<K, V>[] newTable = new Node[capacity];
        table = newTable;
        size = 0;
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean checkBucket(int bucket, int hash, K key, V value) {
        if (table[bucket] != null) {
            Node<K, V> start = table[bucket];
            do {
                if (start.hash == hash && (start.key == key
                        || key != null && start.key != null
                        && start.key.equals(key))) {
                    start.value = value;
                    return false;
                }
                start = start.next;
            } while (start != null);
        }
        return true;
    }

    private void addInBucket(Node<K, V> node, int bucket) {
        Node<K, V> last = table[bucket];
        while (last.next != null) {
            last = last.next;
        }
        last.next = node;
        size++;
    }

    private final int hashKey(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    private class Node<K, V> {
        private int hash;
        private K key;
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
