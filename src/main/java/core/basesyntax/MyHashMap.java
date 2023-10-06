package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K,V> [] table;
    private int size;
    private int newThr = 12;
    private int capacity = DEFAULT_CAPACITY;
    private class Node <K,V> {
        private final int hashCode;
        private final K key;
        private V value;
        private Node<K,V> next;
        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.hashCode = key.hashCode();
            this.next = next;
        }

    }


    @Override
    public void put(K key, V value) {
        if (key == null && table[0] != null) {
            table[0] = new Node<>(key, value, null);
            size++;
            return;
        }
        if (++size > newThr) {
            resize();
        }
        int index = (int) hashCode(key) % capacity;
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            connectNode(key, value, index, table);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        for (Node<K,V> node : table) {
            if (node.key.equals(key)) {
                return node.value;
            } else if (node.key == null) {
                return table[0].value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashCode(K key) {
        int result = 31;
        result = 31 * (key == null? 0 : key.hashCode());
        return result;
    }

    private void resize() {
        capacity = capacity * 2;
        Node<K, V> [] newTable = new Node[capacity];
        newThr = newThr * 2;
        for (Node<K, V> node : table) {
            int indexResize = (int) node.key.hashCode() % capacity;
            if (newTable[indexResize] == null) {
                newTable[indexResize] = new Node<>(node.key, node.value, null);
            } else {
               connectNode(node.key, node.value, indexResize, newTable);
            }
        }
        table = newTable;
    }

    private void addToBucket(K key, V value, int index, Node<K,V> [] typeTable) {
        Node<K,V> resultNode = new Node<>(key, value, null);
        Node<K,V> currentNode = typeTable[index];
        while (currentNode.next != null || currentNode.key != key) {
            currentNode = currentNode.next;
        }
        if (currentNode.key == key) {
            currentNode.value = value;
            return;
        }
        resultNode = currentNode.next;
        currentNode.next = resultNode;
    }
}
