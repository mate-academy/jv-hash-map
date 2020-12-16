package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    Node<K, V>[] bucketArray = new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (size + 1 > bucketArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int bucket = bucket(key);
        if (bucketArray[bucket] == null) { //empty bucket
            bucketArray[bucket] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> current = bucketArray[bucket];
            while (1 == 1) {
                if (key == null ? key == current.key : key.equals(current.key)) { //if keys equals change old value to new
                    current.value = value;
                    return;
                } else {
                    if (current.next == null) { //if its end of a list put new Node
                        current.next = new Node<>(key, value);
                        size++;
                        return;
                    } else {
                        current = current.next;
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = bucket(key);
        Node<K, V> current = bucketArray[bucket];
        while (1 == 1) {
            if (current == null) {
                return null;
            }
            if (key == null ? key == current.key : key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucket(K key) {
        int result = key == null ? 0 : key.hashCode() % 16;
        return Math.abs(result);
    }

    private void resize() {
        Node<K, V>[] oldArray = bucketArray;
        bucketArray = new Node[oldArray.length * 2];
        for (Node<K, V> n : oldArray) {
            if (n != null) {
                put(n.key, n.value);
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
