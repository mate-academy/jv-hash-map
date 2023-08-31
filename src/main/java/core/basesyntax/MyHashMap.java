package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int ARRAY_DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final double LOAD_COEFFICIENT = 0.75;
    private int threshold;
    private int size;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[ARRAY_DEFAULT_CAPACITY];
        threshold = (int) (array.length * LOAD_COEFFICIENT);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> bucketNode = new Node<>(key, value);
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        if (array[index] == null) {
            array[index] = bucketNode;
            size++;
            return;
        }

        Node<K, V> current = array[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key) || current.key == key) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                bucketNode.next = array[index];
                array[index] = bucketNode;
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = array[index];
        while (current != null) {
            if (current.key == key
                    || current.key != null && current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldArray = array;
        array = new Node[array.length * CAPACITY_MULTIPLIER];
        for (Node<K, V> kvNode : oldArray) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % array.length;
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
