package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int keyIndex = getPlace(key);
        Node<K,V> currentNode = table[keyIndex];
        if (currentNode == null) {
            table[keyIndex] = new Node<>(key, value, null);
            size++;
            return;
        } else {
            while (currentNode != null) {
                if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
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
    }

    @Override
    public V getValue(K key) {
        int place = getPlace(key);
        Node<K, V> currentNode = table[place];
        while (currentNode != null) {
            if (key == currentNode.key || currentNode.key != null
                    && currentNode.key.equals(key)) {
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

    private int getPlace(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * MULTIPLIER];
        size = 0;
        threshold = threshold * MULTIPLIER;
        for (Node<K, V> bucket : oldTable) {
            Node<K, V> currentNode = bucket;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
