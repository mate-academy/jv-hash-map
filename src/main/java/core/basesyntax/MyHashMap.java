package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int capasity;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        capasity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (isMapLoad()) {
            resize();
        }
        table = putElement(key, value, table);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> curentNode = table[getIndex(key)];
        while (curentNode != null) {
            if (campareKeys(curentNode.key, key)) {
                return curentNode.value;
            }
            curentNode = curentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length << 1];
        capasity = capasity << 1;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            Node<K, V> curentNode = table[i];
            while (curentNode != null) {
                newTable = putElement(curentNode.key, curentNode.value, newTable);
                curentNode = curentNode.next;
            }
        }
        table = newTable;
    }

    private Node<K, V>[] putElement(K key, V value, Node<K, V>[] table) {
        Node<K, V> newElement = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] != null) {
            if (isKeyPresent(key, table)) {
                changeElementInNodeList(table[index], newElement);
                return table;
            }
            putToNodeList(table[index], newElement);
            size++;
            return table;
        }
        table[index] = newElement;
        size++;
        return table;
    }

    private void putToNodeList(Node<K, V> curentNode, Node<K, V> addNode) {
        Node<K, V> node = curentNode;
        while (node.next != null) {
            node = node.next;
        }
        node.next = addNode;
    }

    private void changeElementInNodeList(Node<K, V> curentNode, Node<K, V> addNode) {
        Node<K, V> node = curentNode;
        while (node != null) {
            if (campareKeys(node.key, addNode.key)) {
                node.value = addNode.value;
                return;
            }
            node = node.next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capasity);
    }

    private boolean isKeyPresent(K key, Node<K, V>[] table) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> curentNode = table[i];
                while (curentNode != null) {
                    if (campareKeys(curentNode.key, key)) {
                        return true;
                    }
                    curentNode = curentNode.next;
                }
            }
        }
        return false;
    }

    private boolean campareKeys(K firstKey, K secondKey) {
        return (firstKey == secondKey) || (firstKey != null) && firstKey.equals(secondKey);
    }

    private boolean isMapLoad() {
        return size == (double)table.length * LOAD_FACTOR;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
