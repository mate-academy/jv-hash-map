package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TWO_FOR_DOUBLING = 2;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive");
        }
        this.loadFactor = loadFactor;
        threshold = (int) (initialCapacity * loadFactor);
        table = new Node[initialCapacity];
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            for (Node<K, V> node : table) {
                while (node != null) {
                    if (node.key == null) {
                        return node.value;
                    }
                    node = node.next;
                }
            }
        } else {
            int index = indexFor(key);
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (key.equals(node.key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = (key == null) ? 0 : indexFor(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(key,value,table[index]);
        size++;
    }

    private int indexFor(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private int indexForNewTable(K key, int length) {
        return Math.abs(key.hashCode()) % length;
    }

    private void resize() {
        int newCapacity = table.length * TWO_FOR_DOUBLING;
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = indexForNewTable(node.key, newTable.length);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private MyHashMap.Node<K, V> next;

        Node(K key, V value, MyHashMap.Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
