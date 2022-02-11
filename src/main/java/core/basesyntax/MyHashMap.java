package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size = 0;
    private int capacity = 16;
    private int threshold = (int) (capacity * LOAD_FACTOR);

    public MyHashMap() {
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {

        if (size == threshold) {
            resizeTable();
        }
        Node<K,V> createdNode = new Node<>(key, value);

        addNoneToTable(createdNode, table);
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int searchingNodeHash = getHash(key);
            int positionInTable = Math.abs(searchingNodeHash) % (capacity - 1);
            Node<K,V> current;
            if (table[positionInTable] != null) {
                current = table[positionInTable];
                do {
                    if ((key == null && current.key == null) || key != null
                            && (searchingNodeHash == current.hash && key.equals(current.key))) {
                        return current.value;
                    }
                    current = current.next;
                } while (current != null);
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;

            if (key == null) {
                this.hash = 0;
            } else {
                this.hash = key.hashCode();
            }
        }
    }

    private void addNodeToList(int number, Node<K,V> addNode) {
        Node<K,V> currentNode = table[number];
        do {
            if ((currentNode.key == null && addNode.key == null) || (currentNode.key != null)
                    && (currentNode.hash == addNode.hash && currentNode.key.equals(addNode.key))) {
                currentNode.value = addNode.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = addNode;
                size++;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);

    }

    private void resizeTable() {
        capacity = capacity * 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;

        Node<K,V>[] oldTable = table;
        table = new Node[capacity];

        for (Node<K,V> current: oldTable) {
            if (current != null) {
                do {
                    Node<K,V> movableNode = new Node<>(current.key, current.value);
                    addNoneToTable(movableNode, table);
                    current = current.next;
                } while (current != null);
            }
        }
    }

    private void addNoneToTable(Node<K,V> node, Node<K,V>[] table) {
        int buckedNumber = Math.abs(node.hash) % (capacity - 1);
        if (table[buckedNumber] == null) {
            table[buckedNumber] = node;
            size++;
        } else {
            addNodeToList(buckedNumber, node);

        }
    }

    private int getHash(K key) {
        int result;
        if (key == null) {
            result = 0;
        } else {
            result = key.hashCode();
        }
        return result;
    }
}
