package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int arrayLength = DEFAULT_LENGTH;
    private int threshold = (int) (arrayLength * LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucketIndex = hash(key);
        Node<K, V> currentNode = table[bucketIndex];

        if (currentNode == null) {
            table[bucketIndex] = new Node<>(key, value, null);
        } else {
            while (currentNode != null) {
                if (key == currentNode.key
                        || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (key == currentNode.key
                    || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] preResize = table;
        table = new Node[preResize.length * 2];
        threshold = (int) ((preResize.length << 2) * LOAD_FACTOR);
        for (Node<K, V> currentNode : preResize) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
