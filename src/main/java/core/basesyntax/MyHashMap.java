package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_FACTOR = 2;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= threshold) {
            resize();
        }

        Node<K, V> newNode = new Node<>(key, value);
        int newNodeIndex = getHash(newNode.key) % capacity;
        if (table[newNodeIndex] == null) {
            table[newNodeIndex] = newNode;
        } else {
            Node<K, V> nodeBefore = table[newNodeIndex];
            while (nodeBefore != null) {
                if (nodeBefore.key == key
                        || (nodeBefore.key != null && nodeBefore.key.equals(key))) {
                    nodeBefore.value = value;
                    return;
                }
                if (nodeBefore.next == null) {
                    nodeBefore.next = newNode;
                    break;
                }
                nodeBefore = nodeBefore.next;
            }
        }
        ++size;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> e = getNode(key);
        return e == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final int oldCapacity = capacity;
        capacity = (capacity == 0) ? INITIAL_CAPACITY : capacity * INCREASE_FACTOR;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {
                Node<K, V> oldNode = oldTable[i];
                while (oldNode != null) {
                    put(oldNode.key, oldNode.value);
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> kvNode;
        for (int i = 0; i < capacity; i++) {
            kvNode = table[i];
            while (kvNode != null) {
                if (kvNode.key == key || (kvNode.key != null && kvNode.key.equals(key))) {
                    return kvNode;
                }
                kvNode = kvNode.next;
            }
        }
        return null;
    }

    private int getHash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

}
