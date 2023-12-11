package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] nodes;
    private int size;
    private int treshold;
    private int maxCap = DEFAULT_INITIAL_CAPACITY;

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            resize();
        }
        int index = getIndex(key);
        Node nodeToPut = new Node<>(key, value);
        if (nodes[index] == null) {
            nodes[index] = nodeToPut;
        } else {
            Node<K, V> current = nodes[index];
            while (current.next != null || current.key == key) {
                if (current.key == key) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current.next = nodeToPut;
        }
        if (++size > treshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0 || nodes[getIndex(key)] == null) {
            return null;
        }
        return nodes[getIndex(key)].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        treshold = (int)(maxCap * DEFAULT_LOAD_FACTOR);
        if (size == 0) {
            nodes = new Node[maxCap];
        } else {
            Node<K, V>[] oldNodesArray = nodes;
            nodes = new Node[maxCap];
            for (int i = 0; i < maxCap; i++) {
                if (oldNodesArray[i] != null) {
                    put(oldNodesArray[i].key, oldNodesArray[i].value);
                }
            }
        }
        maxCap = maxCap * 2;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % nodes.length;
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
