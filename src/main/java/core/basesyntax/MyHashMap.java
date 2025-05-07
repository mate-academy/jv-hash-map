package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int START_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[START_CAPACITY];
        threshold = (int) (START_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (table[index] != null) {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key == key || current.key != null && current.key.equals(key)) {
                    current.value = value;
                    break;
                }
                if (current.next == null) {
                    current.next = new Node<>(null, value, key);
                    size++;
                    break;
                }
                current = current.next;
            }
        } else {
            table[index] = new Node<>(null, value, key);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K,V> {
        private Node<K, V> next;
        private V value;
        private K key;

        private Node(Node<K, V> next, V value, K key) {
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length << 1];
            threshold = threshold << 1;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }
}
