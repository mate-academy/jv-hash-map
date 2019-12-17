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
    private int capacity;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        sizeCheck();
        int hash = hashKey(key);
        int bucket = hash % capacity;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[bucket] == null) {
            table[bucket] = newNode;
            size++;
            return;
        }
        if (!overwriteDuplicates(bucket, key, value)) {
            addInBucket(newNode, bucket);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hashKey(key);
        int bucket = hash % capacity;
        if (table[bucket] == null) {
            return null;
        }
        Node<K, V> node = table[bucket];
        do {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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
        if (size > capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        capacity = capacity << 1;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean overwriteDuplicates(int bucket, K key, V value) {
        if (table[bucket] != null) {
            Node<K, V> start = table[bucket];
            do {
                if (start.key == key || start.key != null && start.key.equals(key)) {
                    start.value = value;
                    return true;
                }
                start = start.next;
            } while (start != null);
        }
        return false;
    }

    private void addInBucket(Node<K, V> node, int bucket) {
        Node<K, V> last = table[bucket];
        while (last.next != null) {
            last = last.next;
        }
        last.next = node;
        size++;
    }

    private int hashKey(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    private static class Node<K, V> {
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
