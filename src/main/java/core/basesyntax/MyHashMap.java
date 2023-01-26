package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int RELOAD_INDEX = 2;
    private static final float RESIZE_COEFFICIENT = 0.75f;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newElement = new Node<>(key, value);
        Node<K, V> element = buckets[index];
        if (element == null) {
            buckets[index] = newElement;
        }
        while (element != null) {
            if (element.key == key || element.key != null && element.key.equals(key)) {
                element.value = value;
                return;
            }
            if (element.next == null) {
                element.next = newElement;
                break;
            }
            element = element.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            Node<K, V> node = buckets[index];
            while (node != null) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
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
        if (size > buckets.length * RESIZE_COEFFICIENT) {
            size = 0;
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[buckets.length * RELOAD_INDEX];
            for (Node<K, V> node : oldBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
