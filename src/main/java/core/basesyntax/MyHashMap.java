package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = countThreshold();
    }

    private MyHashMap(int capacity) {
        table = new Node[capacity];
        threshold = countThreshold();
    }

    @Override
    public void put(K key, V value) {
        int idx = getIndex(key);
        Node<K, V> bucket = table[idx];
        Node<K, V> existing = getNode(key);
        if (bucket == null || existing == null) {
            table[idx] = new Node<>(key, value, bucket);
        } else {
            existing.val = value;
            return;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node != null ? node.val : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return Math.floorMod(hash, table.length);
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> runner = table[getIndex(key)];
        while (runner != null) {
            if (runner.key == key || key != null && key.equals(runner.key)) {
                return runner;
            }
            runner = runner.next;
        }
        return null;
    }

    private int countThreshold() {
        return (int) (table.length * LOAD_FACTOR);
    }

    private void resize() {
        MyHashMap<K, V> map = new MyHashMap<>(table.length << 1);
        for (Node<K, V> node : table) {
            while (node != null) {
                map.put(node.key, node.val);
                node = node.next;
            }
        }
        table = map.table;
        threshold = countThreshold();
    }

    private static class Node<K, V> {
        private final K key;
        private V val;
        private final Node<K, V> next;

        public Node(K key, V val, Node<K, V> next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }
}
