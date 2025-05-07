package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> currentNode = null;
            do {
                currentNode = currentNode == null ? table[index] : currentNode.next;
                if (key == null && currentNode.key == null
                        || key != null && key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
            } while (currentNode.next != null);
            currentNode.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> x = table[index]; x != null; x = x.next) {
            if (key == null && x.key == null || key != null && key.equals(x.key)) {
                return x.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int capacity = table.length;
        if (size < capacity * LOAD_FACTOR) {
            return;
        }
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            for (Node<K, V> currentNode = node; currentNode != null;
                    currentNode = currentNode.next) {
                put(currentNode.key, currentNode.value);
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
