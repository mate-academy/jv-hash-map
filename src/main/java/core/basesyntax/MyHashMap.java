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
        resize();
        int hash = hash(key);
        Node<K, V> current = table[hash];
        Node<K, V> newNode = new Node<>(key, value);
        if (current == null) {
            table[hash] = newNode;
        } else {
            while (current != null) {
                if (elementsAreEqual(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
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

    private boolean elementsAreEqual(K a, K b) {
        return a == b || a != null && a.equals(b);
    }

    private void resize() {
        if (size >= (table.length * DEFAULT_LOAD_FACTOR)) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> element : oldTable) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
