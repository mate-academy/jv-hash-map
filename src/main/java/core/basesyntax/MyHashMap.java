package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int capacity;
    private float loadFactor;
    private Node<K, V>[] data;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int mapCapacity, float mapLoadFactor) {
        capacity = mapCapacity;
        loadFactor = mapLoadFactor;
        data = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int index = getContainerIndex(key);
        if (data[index] == null) {
            data[index] = new Node<K, V>(key, value, null);
        } else {
            for (Node<K, V> node = data[index]; node != null; node = node.next) {
                if (key == node.key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
            }
            data[index] = new Node<K, V>(key, value, data[index]);
        }
        size++;
        if (size >= capacity * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getContainerIndex(key);
        if (data[index] == null) {
            return null;
        }
        for (Node<K, V> node = data[index]; node != null; node = node.next) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getContainerIndex(K key) {
        return (key == null) ? 0 : key.hashCode() & (capacity - 1);
    }

    private void locateNode(Node<K, V> node) {
        int index = getContainerIndex(node.key);
        if (data[index] == null) {
            data[index] = node;
            node.next = null;
        } else {
            node.next = data[index];
            data[index] = node;
        }
    }

    private void resize() {
        Node[] oldHashTable = data;
        capacity = capacity << 1;
        data = new Node[capacity];

        for (int i = 0; i < oldHashTable.length; i++) {
            for (Node<K, V> node = oldHashTable[i]; node != null;) {
                Node<K, V> oldNextNode = node.next;
                locateNode(node);
                node = oldNextNode;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K nodeKey, V nodeValue, Node<K, V> nodeNext) {
            key = nodeKey;
            value = nodeValue;
            next = nodeNext;
        }
    }
}
