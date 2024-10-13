package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {

        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hashCode = key.hashCode();
        int index = Math.abs(hashCode % table.length);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> currentNode = node;
        while (true) {
            if (key.equals(currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {

        if (size == 0) {
            return null;
        }
        if (key == null) {
            return getValueForNullKey();
        }
        int index = Math.abs(key.hashCode() % table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key.equals(node.key)) {
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
        size = 0;
        Node<K, V>[] newTable = new Node[table.length * 2];
        Node<K, V>[] oldTable = table;
        table = newTable;

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private V getValueForNullKey() {
        Node<K, V> node = table[0];
        while (node != null) {
            if (node.key == null) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private void putForNullKey(V value) {
        Node<K, V> node = table[0];
        if (node == null) {
            table[0] = new Node<>(null, value, null);
            size++;
        } else {
            Node<K, V> current = node;
            while (true) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(null, value, null);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }
}
