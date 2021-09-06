package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (checkCapacity()) {
            resize();
        }
        addNode(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> suppNode = table[getIndex(key)];
        while (suppNode != null) {
            if (checkKeys(suppNode.key, key)) {
                return suppNode.value;
            }
            suppNode = suppNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private boolean checkCapacity() {
        return size == threshold;
    }

    private Node<K, V>[] resize() {
        size = 0;
        threshold *= RESIZE_COEFFICIENT;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_COEFFICIENT];
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                addNode(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private void addNode(K key, V value) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (checkKeys(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[getIndex(key)] = new Node<>(key, value, null);
        size++;
    }

    private int getIndex(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private boolean checkKeys(K firstKey, K secondKey) {
        return (firstKey == secondKey || (firstKey != null && firstKey.equals(secondKey)));
    }
}
