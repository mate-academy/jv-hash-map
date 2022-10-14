package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROW_INDEX = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

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

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = table[getIndex(key)];
        if (node == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
        }
        while (node != null) {
            if (isEquals(node, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        return getNode(key) != null ? getNode(key).value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (isEquals(node, key)) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] tab = table;
        table = new Node[table.length * GROW_INDEX];
        threshold *= GROW_INDEX;
        for (Node<K, V> node : tab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return hash(key) & (table.length - 1);
    }

    private boolean isEquals(Node<K, V> node, K key) {
        return node.key == key || node.key != null
                && node.key.hashCode() == hash(key)
                && node.key.equals(key);
    }
}
