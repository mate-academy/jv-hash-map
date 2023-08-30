package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int currentCapacity = DEFAULT_CAPACITY;
    private int size;
    private Node<K,V>[] buckets;

    public MyHashMap() {
        buckets = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == currentCapacity * LOAD_FACTOR) {
            grow();
        }
        int keyHashCode = key == null ? 0 : Math.abs(key.hashCode());
        int index = keyHashCode % currentCapacity;
        if (buckets[index] != null) {
            if (!(key == buckets[index].key || key != null && key.equals(buckets[index].key))) {
                Node<K, V> node = buckets[index];
                while (node.next != null) {
                    node = node.next;
                    if (key == node.key || key != null && key.equals(node.key)) {
                        node.value = value;
                        return;
                    }
                }
                node.next = new Node<>(keyHashCode, key, value, null);
                size++;
            } else {
                buckets[index].value = value;
            }
        } else {
            buckets[index] = new Node<>(keyHashCode, key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : Math.abs(key.hashCode() % currentCapacity;
        Node<K, V> node = buckets[index];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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

    private void grow() {
        Node<K, V>[] copyArray = (Node<K, V>[]) new Node[currentCapacity];
        System.arraycopy(buckets, 0, copyArray, 0, currentCapacity);
        currentCapacity *= 2;
        buckets = (Node<K, V>[]) new Node[currentCapacity];
        Node<K, V> node;
        size = 0;
        for (Node<K, V> bucket : copyArray) {
            if (bucket != null) {
                node = bucket;
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
