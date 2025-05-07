package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] nodes;
    private int size;
    private int threshold;
    private int maxCap;

    public MyHashMap() {
        nodes = new Node[DEFAULT_INITIAL_CAPACITY];
        maxCap = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> nodeToPut = new Node<>(key, value);
        if (nodes[index] == null) {
            nodes[index] = nodeToPut;
            size++;
        } else {
            Node<K, V> current = nodes[index];
            while (current.next != null || (current.key != null && current.key.equals(key))
                || key == null) {
                if (key == current.key || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = nodeToPut;
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0 || nodes[getIndex(key)] == null) {
            return null;
        }
        Node<K, V> currentNode = nodes[getIndex(key)];
        while (currentNode != null) {
            if (key == currentNode.key || key != null && key.equals(currentNode.key)) {
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
        maxCap = maxCap * 2;
        threshold = (int)(maxCap * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldNodesArray = nodes;
        nodes = new Node[maxCap];
        int oldCap = maxCap / 2;
        for (int i = 0; i < oldCap; i++) {
            Node<K, V> current = oldNodesArray[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % maxCap;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
