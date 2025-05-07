package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int INDEX_NULL_KEY = 0;
    private Node<K, V>[] values;
    private int size;
    private int modCount;

    public MyHashMap() {
        modCount = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        values = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (isNeedResize()) {
            resizeValues();
            putAfter(key, value, values);
        } else if (key == null) {
            if (values[INDEX_NULL_KEY] == null) {
                size++;
            }
            values[INDEX_NULL_KEY] = new Node<K, V>(key, value, null);
        } else {
            putAfter(key, value, values);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return values[INDEX_NULL_KEY].value;
        }
        V value = null;
        for (int i = 1; i < values.length; i++) {
            for (Node<K, V> current = values[i]; current != null; current = current.next) {
                if (current.key.equals(key)) {
                    value = current.value;
                }
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeValues() {
        Node<K, V>[] oldValues = values;
        modCount = (int) ((oldValues.length * 2) * DEFAULT_LOAD_FACTOR);
        values = (Node<K, V>[]) new Node[oldValues.length * 2];
        int oldSize = size;
        for (int i = 0; i < oldValues.length; i++) {
            for (Node<K, V> current = oldValues[i]; current != null; current = current.next) {
                put(current.key, current.value);
            }
        }
        size = oldSize;
    }

    private int getHash(Object key) {
        int hash = key == null ? 0 : key.hashCode() % values.length;
        hash += hash == 0 ? 1 : 0;
        return hash < 0 ? hash * (-1) : hash;
    }

    private boolean isNeedResize() {
        return modCount == size;
    }

    private boolean isEmptyBucket(K key, Node<K, V>[] node) {
        int index = getHash(key) == 0 ? 1 : getHash(key);
        return node[index] == null;
    }

    private void putAfter(K key, V value, Node<K, V>[] node) {
        int index = getHash(key) == 0 ? 1 : getHash(key);
        if (isEmptyBucket(key, node)) {
            node[index] = new Node<K, V>(key, value, null);
        } else {
            Node<K, V> current = node[index];
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current.next = new Node<K, V>(key, value, null);
        }
        size++;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
