package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int GROW_FACTOR = 2;
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private Node<K, V> nullKeyNode;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            int index = getIndex(key);
            addOrUpdateNode(index, key, value);
            resizeIfNeeded();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyNode == null ? null : nullKeyNode.value;
        }

        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        if (nullKeyNode == null) {
            size++;
        }
        nullKeyNode = new Node<>(null, value, null);
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void addOrUpdateNode(int index, K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> existingNode = table[index];

        if (existingNode == null) {
            table[index] = newNode;
            size++;
        } else {
            while (existingNode != null) {
                if (existingNode.key.equals(key)) {
                    existingNode.value = value;
                    return;
                }
                if (existingNode.next == null) {
                    existingNode.next = newNode;
                    size++;
                    break;
                }
                existingNode = existingNode.next;
            }
        }
    }

    private void resizeIfNeeded() {
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * GROW_FACTOR];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
