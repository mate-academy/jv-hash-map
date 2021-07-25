package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;

    private Node<K, V>[] buckets = new Node[DEFAULT_CAPACITY];
    private int size = 0;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> currentNode = getNode(key);
        if (currentNode != null) {
            currentNode.value = value;
            return;
        }
        int index = getIndex(key, buckets.length);
        Node<K, V> node = buckets[index];
        Node<K, V> prev = node;

        while (node != null) {
            prev = node;
            node = node.next;
        }
        node = new Node<>((key == null) ? 0 : key.hashCode(), key, value, null);
        if (prev != null) {
            prev.next = node;
        }
        if (buckets[index] == null) {
            buckets[index] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNode(key);
        return (currentNode == null) ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size <= threshold) {
            return;
        }

        Node<K, V>[] newBuckets = new Node[buckets.length * 2];
        threshold = (int) (newBuckets.length * LOAD_FACTOR);

        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> oldCurrentNode = buckets[i];
            if (oldCurrentNode == null) {
                continue;
            }
            while (oldCurrentNode != null) {

                int index = getIndex(oldCurrentNode.key, newBuckets.length);
                Node<K, V> newCurrentNode = newBuckets[index];
                Node<K, V> newPrevNode = newCurrentNode;

                while (newCurrentNode != null) {
                    newPrevNode = newCurrentNode;
                    newCurrentNode = newCurrentNode.next;
                }
                newCurrentNode = new Node<>((oldCurrentNode.key == null)
                        ? 0 : oldCurrentNode.key.hashCode(), oldCurrentNode.key,
                        oldCurrentNode.value, null);
                if (newPrevNode != null) {
                    newPrevNode.next = newCurrentNode;
                }
                if (newBuckets[index] == null) {
                    newBuckets[index] = newCurrentNode;
                }
                oldCurrentNode = oldCurrentNode.next;
            }
        }
        buckets = newBuckets;
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key, buckets.length);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private int getIndex(K key, int capacity) {
        int index = (key == null) ? 0 : key.hashCode() % capacity;
        if (index < 0) {
            index += capacity;
        }
        return index;
    }

}
