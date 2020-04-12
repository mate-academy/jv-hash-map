package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private int size;
    private float threshold;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        buckets = new Node[CAPACITY];
        threshold = CAPACITY * DEFAULT_LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (isEqualKey(node, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node = new Node<K, V>(key, value, buckets[index]);
        buckets[index] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (isEqualKey(node, key)) {
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

    private void resize() {
        Node<K, V>[] bigNodes = new Node[buckets.length * 2];
        threshold = buckets.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        Node<K, V>[] littleNodes = buckets;
        buckets = bigNodes;
        for (Node<K, V> node : littleNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean isEqualKey(Node<K, V> node, K key) {
        return key == node.key || key != null && key.equals(node.key);
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
