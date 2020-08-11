package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int capacity;
    private int size;
    private Node<K, V>[] map;

    public MyHashMap() {
        size = 0;
        map = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> currentNode = findNode(key);
        if (currentNode == null) {
            Node<K, V> newNode = new Node<>(key, value);
            map[size + 1] = newNode;
            size++;
        } else {
            currentNode.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = findNode(key);
        if (currentNode != null) {
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(K newKey) {
        for (Node<K, V> node : map) {
            if (node != null) {
                if (node.key == newKey || node.key != null && node.key.equals(newKey)) {
                    return node;
                }
            }
        }
        return null;
    }

    private void resize() {
        if (size >= DEFAULT_LOAD_FACTOR * capacity) {
            Node<K, V>[] newMap = new Node[capacity * 2];
            if (capacity >= 0) {
                System.arraycopy(map, 0, newMap, 0, capacity);
            }
            map = newMap;
            capacity *= 2;
        }
    }

    private static class Node<K, V> {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
