package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_VOLUME = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_VOLUME];
        capacity = INITIAL_VOLUME;
    }

    @Override
    public void put(K key, V value) {
        if ((capacity * LOAD_FACTOR) <= size) {
            resize();
        }
        int index = calculateIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
            size++;
        } else {
            while (node != null) {
                if (node.key == key
                        || (key != null && key.equals(node.key))) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        if (table[index] != null) {
            Node<K, V> node = table[index];
            while (node != null) {
                if (node.key == key
                        || (key != null && key.equals(node.key))) {
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

    private void resize() {
        capacity <<= 1;
        Node<K, V>[] tempTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : tempTable) {
            while (node != null) {
                put(node.key,node.value);
                node = node.next;
            }
        }
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
