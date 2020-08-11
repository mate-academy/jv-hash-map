package core.basesyntax;


/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] table;
    private static final int INITIAL_CAPACITY = 16;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        capacity = INITIAL_CAPACITY;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            int h = hash(key);
            int index = indexFor(h, capacity);
            Node<K, V> node = new Node<>(h, key, value, null);
            Node<K, V> nodes = table[index];
            if(nodes == null){
                addNode(h, key, value, index);
                size++;
                return;
            }
            if (nodes.hash == node.hash && (nodes.key == node.key || node.key.equals(nodes.key))) {
                nodes.value = value;
                return;
            }
            while(nodes.next != null) {
                nodes = nodes.next;
                if (nodes.hash == node.hash && (nodes.key == node.key || node.key.equals(nodes.key))) {
                    nodes.value = value;
                    return;
                }
            }
             nodes = table[index];
                if (nodes.hash == node.hash && nodes.key != node.key) {
                    addNode(h, key, value, index);
                    size++;
                    return;
                }

            addNode(h, key, value, index);
            size++;
        }
    }

    private void putForNullKey(V value) {
        Node<K, V> node = table[0];
        if (node.key == null) {
            node.value = value;
            return;
        }
        while (node.next!=null) {
            node = node.next;
            if (node.key == null) {
                node.value = value;
                return;
            }
        }
        addNode(0, null, value, 0);
        size++;
    }

    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(hash, key, value, node);
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
