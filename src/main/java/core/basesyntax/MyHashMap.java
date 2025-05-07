package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        putIntoTableOrReplaceValue(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (elementsAreEqual(node.key, key)) {
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

    private void putIntoTableOrReplaceValue(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (elementsAreEqual(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
    }

    private boolean elementsAreEqual(K first, K second) {
        return first == second || first != null && first.equals(second);
    }

    private int calculateIndex(K key) {
        return Math.abs(hash(key) % table.length);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        Node[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putIntoTableOrReplaceValue(node.key, node.value);
                node = node.next;
            }
        }
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
