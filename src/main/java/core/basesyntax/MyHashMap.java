package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        int hash = hash(key);
        int indexBucket = (capacity - 1) & hash;
        K nodeKey;
        for (Node<K, V> node = table[indexBucket]; node != null; node = node.next) {
            if (hash == node.hash && ((nodeKey = node.key) == key
                    || key != null && key.equals(nodeKey))) {
                node.value = value;
                return;
            }
        }
        table[indexBucket] = new Node<>(hash, key, value, table[indexBucket]);
        if (size++ >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int hash = hash(key);
            int indexBucket = (capacity - 1) & hash;
            K nodeKey;
            for (Node<K, V> node = table[indexBucket]; node != null; node = node.next) {
                if (hash == node.hash && ((nodeKey = node.key) == key
                        || key != null && key.equals(nodeKey))) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        if (table == null) {
            capacity = DEFAULT_CAPACITY;
            threshold = (int) (capacity * LOAD_FACTOR);
            table = new Node[capacity];
            return;
        }
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[(capacity = capacity * 2)];
        threshold = (int) (capacity * LOAD_FACTOR);
        for (Node<K, V> oldNode : oldTable) {
            for (; oldNode != null; oldNode = oldNode.next) {
                put(oldNode.key, oldNode.value);
            }
        }
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
