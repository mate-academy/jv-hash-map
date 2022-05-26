package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int INDEX_NULL_KEY = 0;
    private Node<K, V>[] table;
    private int size;
    private int modCount;
    private int capacity;


    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        modCount = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (isNeedResize()) {
            resizeTable();
        } else if (key == null) {
            table[INDEX_NULL_KEY] = new Node<K, V>(INDEX_NULL_KEY, key, value, null);
            size++;
        } else {
            int index = getHash(key) == 0 ? 1 : getHash(key);
            putAfter(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putVal(K key, V value, Node<K, V>[] node) {
        if (isNeedResize()) {
            resizeTable();
        } else if (key == null) {
            node[INDEX_NULL_KEY] = new Node<K, V>(INDEX_NULL_KEY, key, value, null);
            size++;
        } else {
            int index = getHash(key) == 0 ? 1 : getHash(key);
            putAfter(key, value, index);
        }
    }


    private void resizeTable() {
        int nextCapacity = capacity * 2;
        modCount = (int) (nextCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[nextCapacity];
        int oldSize = size;
        for (int i = 0; i < capacity; i++) {
            for (Node<K, V> current = table[i]; current != null; current = current.next) {
                if (current != null) {
                    put(current.key, current.value);
                }
            }
        }
        this.table = newTable;
        size = oldSize;
        //!! Here to be continues
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode() % capacity;
    }

    private boolean isNeedResize() {
        return modCount == size;
    }

    private boolean isCollision(Object key, Node<K, V> node) {
        return getHash(key) == (node == null ? 0 : node.hash);
    }

    private boolean isEmptyBucket(K key) {
        int index = getHash(key) == 0 ? 1 : getHash(key);
        return table[index] == null;
    }


    private void putAfter(K key, V value, int index) {
        index = getHash(key) == 0 ? 1 : getHash(key);
        if (isEmptyBucket(key)) {
            table[index] = new Node<K, V>(index, key, value, null);
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current.next = new Node<K, V>(getHash(key), key, value, null);
        }
        size++;
    }


    private static class Node<K, V> {
        private final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
