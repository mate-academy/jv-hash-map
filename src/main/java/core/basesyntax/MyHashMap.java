package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final float LOAD_FACTOR = 0.75f;
    public static final int INITIAL_ARRAY_SIZE = 16;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_ARRAY_SIZE];
        threshold = (int) (LOAD_FACTOR * INITIAL_ARRAY_SIZE);
    }

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = (key == null) ? 0 : key.hashCode();
        Node<K, V> node = table[indexByHash(key)];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(hash, key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[indexByHash(key)] = new Node<>(hash, key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[indexByHash(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int oldCapacity = table.length;
        Node<K, V>[] oldTable = table;
        table = new Node[oldCapacity << 1];
        for (Node<K, V> kvNode : oldTable) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    private int indexByHash(K key) {
        int index = (key == null) ? 0 : (key.hashCode() % table.length);
        return index < 0 ? -1 * index : index;
    }
}
