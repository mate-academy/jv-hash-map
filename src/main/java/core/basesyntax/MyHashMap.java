package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int SIZE_INCREASE_INDEX = 2;
    private int capacityTable = 16;
    private int threshold = (int) (capacityTable * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[capacityTable];
    }

    @Override
    public void put(K key, V value) {
        resize();
        putInEmptyBox(key, value, findIndex(key));
        putInNotEmptyBox(key, value, findIndex(key));
    }

    private void putInEmptyBox(K key, V value, int index) {
        if (table[index] == null) {
            table[findIndex(key)] = new Node<>(key, value, null);
            size++;
        }
    }

    private void putInNotEmptyBox(K key, V value, int index) {
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (node.key == key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    break;
                }
                node = node.next;
            }
            size++;

        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[findIndex(key)];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
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

    private void resize() {
        if (size == threshold) {
            final Node<K, V>[] oldTable = table;
            size = 0;
            capacityTable *= SIZE_INCREASE_INDEX;
            countThreshold();
            table = new Node[capacityTable];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }

    }

    private void countThreshold() {
        threshold = (int) (capacityTable * DEFAULT_LOAD_FACTOR);
    }

    private int findIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacityTable);
    }

    static class Node<K, V> {
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
