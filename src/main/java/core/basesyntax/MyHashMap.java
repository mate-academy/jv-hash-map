
package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (keysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> current = table[index];

        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key, int capacity) {
        if (key == null) {
            return 0;
        }

        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    private boolean keysEqual(K first, K second) {
        if (first == second) {
            return true;
        }
        return first != null && first.equals(second);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];

        for (Node<K, V> head : oldTable) {
            Node<K, V> current = head;

            while (current != null) {
                Node<K, V> next = current.next;
                int index = getIndex(current.key, table.length);

                current.next = table[index];
                table[index] = current;

                current = next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
