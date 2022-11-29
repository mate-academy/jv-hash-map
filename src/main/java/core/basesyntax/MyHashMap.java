package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        threshold = countThreshold();
    }


    @Override
    public void put(K key, V value) {
        int idx = getIndex(key);
        Node<K, V> bucket = table[idx];
        Node<K, V> existing = getNode(key);
        if (bucket != null && existing != null) {
            existing.val = value;
            return;
        }
        table[idx] = new Node<>(key, value, bucket);
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
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        threshold = countThreshold();
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.val);
                node = node.next;
            }
        }
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
