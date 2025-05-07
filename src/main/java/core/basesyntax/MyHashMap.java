package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DOUBLE_SIZE = 2;
    private Node<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index];

        if (node == null) {
            buckets[index] = newNode;
            size++;

        } else {
            while (node != null) {
                if ((node.key == key || node.key != null && node.key.equals(key))) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index];

        while (node != null) {
            if (keyequals(node.key, key)) {
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

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private boolean keyequals(K key, K anotherkey) {
        return (key == anotherkey || (key != null && key.equals(anotherkey)));
    }

    private void resize() {
        if (size > buckets.length * LOAD_FACTOR) {
            Node<K, V>[] oldbuckets = buckets;
            buckets = new Node[oldbuckets.length * DOUBLE_SIZE];
            size = 0;

            for (Node<K, V> node : oldbuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
