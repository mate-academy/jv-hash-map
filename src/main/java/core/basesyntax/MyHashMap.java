package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROWTH_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this.buckets = new Node[INITIAL_CAPACITY];
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndexByKey(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (keyEquals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> currentNode = buckets[index];
        while (currentNode != null) {
            if (keyEquals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> node = buckets[index];
        if (node == null) {
            buckets[index] = newNode;
        } else {
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = buckets;
        buckets = new Node[oldTable.length * GROWTH_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (buckets.length * LOAD_FACTOR);
    }

    private int getIndexByKey(K key) {
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        return hash % buckets.length;
    }

    private boolean keyEquals(K key1, K key2) {
        return key1 == null ? key2 == null : key1.equals(key2);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
