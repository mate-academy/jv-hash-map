package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] buckets = new Node[INITIAL_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        resize();
        int index = getNodeIndex(key);
        Node<K, V> node = buckets[index];

        if (node == null) {
            buckets[index] = new Node<>(key, value);
            size++;
        } else {
            while (true) {
                if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : buckets) {
            for (Node<K, V> current = node; current != null; current = current.next) {
                if ((node.key == null && key == null)
                        || (node.key != null && node.key.equals(key))) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > threshold) {
            increase();
        }
    }

    private void increase() {
        int newCapacity = buckets.length * MULTIPLIER;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = buckets;
        buckets = new Node[newCapacity];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getNodeIndex(K key) {
        return (key == null) ? 0 : Math.abs((key.hashCode() % buckets.length));
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
