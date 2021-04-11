package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int currentCapacity;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (checkLoadOfMap()) {
            table = resizeNodesArray();
        }
        if (addNodeToArray(key, value, table)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null ? 0 : generateHashCodeByKey(key));
        Node<K, V> node = table[hash];
        if (node == null) {
            return null;
        }
        if (key == node.key) {
            return table[hash].value;
        }
        while (!node.key.equals(key)) {
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int generateHashCodeByKey(K key) {
        if (key == null) {
            return 0;
        }
        int result = 17;
        result = 31 * result + Math.abs(key.hashCode());
        result = result % currentCapacity;
        return result;
    }

    private boolean putNodeWithExistedHash(Node<K, V> newNode, Node<K, V> nodeInTable) {
        Node<K, V> previousNode = null;
        while (nodeInTable != null) {
            if (nodeInTable.equals(newNode)) {
                return false;
            }
            previousNode = nodeInTable;
            nodeInTable = nodeInTable.next;
        }
        previousNode.next = newNode;
        return true;
    }

    private boolean checkLoadOfMap() {
        return size == threshold;
    }

    private Node<K, V>[] resizeNodesArray() {
        currentCapacity *= 2;
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
        return replaceDataToResizedArray();
    }

    private Node[] replaceDataToResizedArray() {
        Node[] enlargedArray = new Node[currentCapacity];
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            }
            addNodeToArray(table[i].key, table[i].value, enlargedArray);
            if (table[i].next != null) {
                table[i] = table[i].next;
                i--;
            }
        }
        return enlargedArray;
    }

    private boolean addNodeToArray(K key, V value, Node<K, V>[] nodes) {
        boolean increasingSize = false;
        int hash = generateHashCodeByKey(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (nodes[hash] == null) {
            nodes[hash] = newNode;
            increasingSize = true;
        } else if (equalsKeys(key, nodes[hash].key)) {
            nodes[hash].value = value;
        } else {
            Node<K, V> nodeInTable = nodes[hash];
            increasingSize = putNodeWithExistedHash(newNode, nodeInTable);
        }
        return increasingSize;
    }

    private boolean equalsKeys(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object node) {
            if (this == node) {
                return true;
            }
            if (node == null) {
                return false;
            }
            if (node.getClass().equals(Node.class)) {
                Node<K, V> currentNode = (Node<K, V>) node;
                return currentNode.key.equals(this.key)
                        && currentNode.value.equals(this.value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + this.key.hashCode();
            result = 31 * result + this.value.hashCode();
            return result;
        }
    }
}
