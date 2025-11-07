package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = (key == null) ? 0 : hash(key);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> current = table[index];
            while (true) {
                if ((current.key == null && key == null)
                        || (current.key != null && current.key.equals(key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                current = current.next;
            }
        }
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == null && key == null) {
                return current.value;
            }
            if (current.key != null && current.key.equals(key)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MyHashMap<?, ?> other = (MyHashMap<?, ?>) o;
        return this.size == other.size;
    }

    private int hash(K key) {
        int hash = (key == null) ? 0 : (key.hashCode() & 0x7fffffff) % table.length;
        return hash;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                int newIndex = (node.key == null)
                        ? 0 : (node.key.hashCode() & 0x7fffffff) % table.length;
                Node<K, V> nextNode = node.next;
                node.next = table[newIndex];
                table[newIndex] = node;
                node = nextNode;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
