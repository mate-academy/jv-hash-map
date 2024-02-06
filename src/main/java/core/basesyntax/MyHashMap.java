package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY_FACTOR = 2;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = hash(key);
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = table[hash];
            while (node != null) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            table[hash] = new Node<>(key, value, table[hash]);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> node = table[hash];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_CAPACITY_FACTOR];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> oldNode : oldTable) {
            Node<K, V> node = oldNode;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
