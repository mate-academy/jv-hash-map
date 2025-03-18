package core.basesyntax;


public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int DEFAULT_CAPACITY = 16;
    private final double DEFAULT_LOAD_FACTOR = 0.75;

    private Node<K,V> [] table;
    private int size;
    private int capacity;
    private int treshhold;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.treshhold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.table = new Node[capacity];
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

    private void updateTrashhold () {
        treshhold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    public void resize() { // 75%
        if (size < treshhold) {
            return;
        }
        capacity *= 2;
        updateTrashhold();

        Node<K,V> [] oldTable = table;
        table = new Node[capacity];

        for (Node<K,V> node : oldTable) {
            while (node != null) {
                Node <K, V> nextNode = node.next;
                int index = (node.key == null) ? 0 : Math.abs(node.key.hashCode()) % capacity;
                node.next = table[index];
                node = nextNode;
            }
        }

    }

    @Override
    public void put(K key, V value) {
        updateTrashhold();
        resize();

        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;

        Node<K, V> current = table [index];
        while (current != null) {
            if ((current.key == null && key == null) || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node <> (key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;

        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == null && key == null) {
                return node.value;
            }
            if (node.key != null && node.key.equals(key)) {
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
}
