package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = getNodeByKey(key);
        if (currentNode != null) {
            currentNode.value = value;
        } else {
            if ((table == null ? 0 : table.length) * LOAD_FACTOR < size + 1) {
                resize();
            }
            currentNode = new Node<>(key, value, null);
            putNode(currentNode);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNodeByKey(key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNodeByKey(K key) {
        int index = getIndex(key);
        if (size == 0 || table[index] == null) {
            return null;
        }
        Node<K, V> currentNode = table[index];
        do {
            if (key == null && currentNode.key == null
                    || key != null && key.equals(currentNode.key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
        return null;
    }

    private int getIndex(K key) {
        return Math.abs(key == null || table == null ? 0 : key.hashCode() % table.length);
    }

    private void putNode(Node<K, V> newNode) {
        int index = getIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
    }

    private void resize() {
        int newCapacity = ((table == null ? INITIAL_CAPACITY : table.length << 1));
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        copyNodes(oldTable);
    }

    private void copyNodes(Node<K, V>[] oldTable) {
        if (oldTable == null) {
            return;
        }
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                putNode(currentNode);
                Node<K, V> prevNode = currentNode;
                currentNode = currentNode.next;
                prevNode.next = null;
            }
            oldTable[i] = null;
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
