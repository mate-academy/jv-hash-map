package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DOUBLE_SIZE = 2;
    private Node<K, V>[] buckets;
    private boolean nullkey = false;
    private V nullvalue;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new Node[DEFAULT];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (!nullkey) {
                size++;
                nullkey = true;
            }
            nullvalue = value;
            return;
        }
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index];

        if (node == null) {
            buckets[index] = new Node<>(key, value, null);
            size++;

        } else {
            while (node != null) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                node = node.next;
            }
        }

        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
        public V getValue(K key) {
        if (key == null) {
            return nullvalue;
        }
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index];

        while (node != null) {
            if (node.key.equals(key)) {
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
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
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
