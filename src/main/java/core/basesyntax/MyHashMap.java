package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_RESIZE = 2;
    private Node<K, V>[] hashTable;
    private int size;
    private float threshold;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = calculateIndex(key);
        Node<K, V> indexNode = hashTable[index];
        Node<K, V> addNode = new Node<>(key, value, null);
        while (indexNode != null) {
            if (key == indexNode.key || (key != null && key.equals(indexNode.key))) {
                indexNode.value = value;
                return;
            }
            if (indexNode.next == null) {
                indexNode.next = addNode;
                size++;
                return;
            }
            indexNode = indexNode.next;
        }
        hashTable[index] = addNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = hashTable[calculateIndex(key)];
        while (getNode != null) {
            if (key == getNode.key || (key != null && key.equals(getNode.key))) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % hashTable.length);
    }

    private void resize() {
        threshold *= DEFAULT_RESIZE;
        size = 0;
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[oldHashTable.length * DEFAULT_RESIZE];
        for (Node<K, V> node : oldHashTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
