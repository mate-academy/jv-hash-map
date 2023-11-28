package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int currentCapacity = DEFAULT_CAPACITY;
    private double threshHold = DEFAULT_CAPACITY * LOAD_FACTOR;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[currentCapacity];
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key);
        int index = hash % currentCapacity;
        if (buckets[index] == null) {
            buckets[index] = new Node<>(hash, key, value, null);
            size++;
        } else if (buckets[index] != null) {
            Node<K, V> currentNode;
            for (currentNode = buckets[index]; currentNode.next != null;
                    currentNode = currentNode.next) {
                if (hasSameKeyOrValue(currentNode, key, value)) {
                    break;
                }
            }
            if (!hasSameKeyOrValue(currentNode, key, value)) {
                currentNode.next = new Node<>(hash, key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> bucket : buckets) {
            for (Node<K, V> currentNode = bucket; currentNode != null;
                    currentNode = currentNode.next) {
                if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
                    return currentNode.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshHold) {
            size = 0;
            currentCapacity *= 2;
            threshHold *= 2;
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[currentCapacity];
            for (Node<K, V> node : oldBuckets) {
                for (Node<K, V> currentNode = node; currentNode != null;
                        currentNode = currentNode.next) {
                    put(currentNode.key, currentNode.value);
                }
            }
        }
    }

    private boolean hasSameKeyOrValue(Node<K, V> currentNode, K key, V value) {
        if ((key == currentNode.key || key != null && key.equals(currentNode.key))
                && (value == currentNode.value || value != null
                && value.equals(currentNode.value))) {
            return true;
        }
        if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
            currentNode.value = value;
            return true;
        }
        return false;
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
