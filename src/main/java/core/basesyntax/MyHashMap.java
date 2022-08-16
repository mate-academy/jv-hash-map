package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int RESIZE = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        }
        while (node != null) {
            if (node.hash == hash(key)
                    && (node.key == key || (key != null && key.equals(node.key)))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(hash(key), key, value, null);
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash(key)
                    && (node.key == key || (key != null && key.equals(node.key)))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getIndex(K key) {
        return hash(key) & (table.length - 1);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * RESIZE];
        threshold = threshold * RESIZE;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
