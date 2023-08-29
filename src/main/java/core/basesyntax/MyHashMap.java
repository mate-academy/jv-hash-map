package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROWTH_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> existingNode = getNodeByKey(key);
        if (existingNode != null) {
            existingNode.value = value;
        } else {
            addNode(key, value, getIndexByKey(key));
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> existingNode = getNodeByKey(key);
        return existingNode == null ? null : existingNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNodeByKey(K key) {
        int index = getIndexByKey(key);
        Node<K, V> currentNode = buckets[index];
        while (currentNode != null) {
            if (areKeysEqual(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = buckets[index];
        if (currentNode == null) {
            buckets[index] = newNode;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
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

    private boolean areKeysEqual(K firstKey, K secondKey) {
        return firstKey == null ? secondKey == null : firstKey.equals(secondKey);
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
