package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (checkThreshold()) {
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
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> curentNode = oldTable[i];
            while (curentNode != null) {
                table = putElement(curentNode.key, curentNode.value, table);
                curentNode = curentNode.next;
            }
        }
    }

    private Node<K, V>[] putElement(K key, V value, Node<K, V>[] table) {
        Node<K, V> newElement = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] != null) {
            putToNodeList(table[index], newElement);
            return table;
        }
        table[index] = newElement;
        size++;
        return table;
    }

    private void putToNodeList(Node<K, V> curentNode, Node<K, V> addNode) {
        Node<K, V> node = curentNode;
        while (node.next != null) {
            if (campareKeys(node.key, addNode.key)) {
                node.value = addNode.value;
                return;
            }
            node = node.next;
        }
        if (campareKeys(node.key, addNode.key)) {
            node.value = addNode.value;
            return;
        }
        node.next = addNode;
        size++;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean campareKeys(K firstKey, K secondKey) {
        return (firstKey == secondKey) || (firstKey != null) && firstKey.equals(secondKey);
    }

    private boolean checkThreshold() {
        return size > table.length * LOAD_FACTOR;
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
