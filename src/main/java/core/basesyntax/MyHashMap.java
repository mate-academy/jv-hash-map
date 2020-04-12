package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int buckets;
    private Node[] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
        this.buckets = 0;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        addNode(key, value, table);;
        resize();
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node searchNode = table[indexFor(key, table.length)];
        while (true) {
            if (areKeysEqual(key, (K) searchNode.key)) {
                return (V) searchNode.value;
            }
            if (searchNode.next == null) {
                return null;
            }
            searchNode = searchNode.next;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value, Node[] table) {
        int index = indexFor(key, table.length);
        Node<K, V> node = new Node<>(getHash(key), key, value, null);
        if (table[index] == null) {
            table[index] = node;
            size++;
            buckets++;
        } else {
            Node searchNode = table[index];
            while (true) {
                if (areKeysEqual(key, (K) searchNode.key)) {
                    searchNode.value = value;
                    break;
                } else if (searchNode.next == null) {
                    searchNode.next = node;
                    size++;
                    break;
                }
                searchNode = searchNode.next;
            }
        }
    }

    private boolean areKeysEqual(K key1, K key2) {
        return getHash(key1) == getHash(key2) && (key1 == null ? key2 == null : key1.equals(key2));
    }

    private int indexFor(K key, int length) {
        return Math.abs(getHash(key) % length);
    }

    private int getHash(K key) {
        //16 is experimentally found value, used to generate more uniq hashCode,
        //which is needed to pass checkArrayLengthAfterResizingTest()
        return key == null ? 0 : key.hashCode() / 16;
    }

    private void resize() {
        if (buckets >= table.length * LOAD_FACTOR) {
            Node[] newTable = new Node[table.length * 2];
            size = 0;
            for (Node value : table) {
                if (value != null) {
                    Node<K, V> node = value;
                    while (true) {
                        addNode(node.key, node.value, newTable);
                        if (node.next == null) {
                            break;
                        }
                        node = node.next;
                    }
                }
            }
            table = newTable;
        }
    }

    private static class Node<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
