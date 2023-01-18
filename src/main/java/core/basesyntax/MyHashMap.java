package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            transfer();
        }
        int hash = getHash(key);
        int index = getIndex(hash);
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (node.hash == hash && (node.key == key || key != null && key.equals(node.key))) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || key != null && key.equals(node.key))) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(int hash) {
        return hash % table.length;
    }

    private void transfer() {
        threshold = threshold * GROW_FACTOR;
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
