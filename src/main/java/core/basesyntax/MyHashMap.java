package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = createTable(DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = findNode(key);
        if (existingNode != null) {
            existingNode.value = value;
            return;
        }

        if ((size + 1) > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        table[index] = new Node<>(key, value, table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (areKeysEqual(key, currentNode.key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.floorMod(key.hashCode(), table.length);
    }

    private boolean areKeysEqual(K firstKey, K secondKey) {
        return firstKey == secondKey || firstKey != null && firstKey.equals(secondKey);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = createTable(oldTable.length * RESIZE_MULTIPLIER);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
