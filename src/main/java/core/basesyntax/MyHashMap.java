package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_COEFFICIENT = 2;
    private int size;
    private Node<K, V>[] buckets;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V getValue(K key) {
        int position = getIndex(key);
        Node<K, V> node = buckets[position];
        while (node != null) {
            if ((key == null && node.key == null)
                    || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        checkBeforePut();
        Node newNode = new Node(key, value, null);
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
        } else {
            Node bucketToPutIn = buckets[index];
            while (bucketToPutIn != null) {
                if ((bucketToPutIn.key == null && newNode.key == null) || (newNode.key != null
                        && getHash(newNode.key) == getHash(bucketToPutIn.key)
                        && newNode.key.equals(bucketToPutIn.key))) {
                    bucketToPutIn.value = newNode.value;
                    return;
                }
                if (bucketToPutIn.next == null) {
                    bucketToPutIn.next = newNode;
                    size++;
                    return;
                }
                bucketToPutIn = bucketToPutIn.next;
            }
        }
    }

    private class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(getHash(key)) % buckets.length;
    }

    private int getHash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void checkBeforePut() {
        if (buckets == null) {
            buckets = new Node[INITIAL_CAPACITY];
        }
        if (size == (int) buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K,V>[] oldHashMap = buckets;
        buckets = new Node[oldHashMap.length * GROWTH_COEFFICIENT];
        size = 0;
        for (Node<K,V> node : oldHashMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean isEmpty() {
        return size == 0;
    }

}
