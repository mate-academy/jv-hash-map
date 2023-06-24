package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;


    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> precededToInsertedNode = table[hash];
            while (true) {
                if (key == null && precededToInsertedNode.key == null
                        || precededToInsertedNode.key != null && precededToInsertedNode.key.equals(key)) {
                    precededToInsertedNode.value = value;
                    return;
                }
                if (precededToInsertedNode.next != null) {
                    precededToInsertedNode = precededToInsertedNode.next;
                } else {
                    break;
                }
            }
            precededToInsertedNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        if (node == null) {
            return null;
        }
        while (node.next != null) {
            if (key == null && node.key == null || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return table[hash(key)].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return 1;
       // return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] resizeTable = new Node[capacity];
        reorganiseNodeDueToResizing(resizeTable);
       // table = resizeTable;
    }

    private void reorganiseNodeDueToResizing(Node<K, V>[] resizeTable) {
        Node<K, V>[] copyTable = table;
        table = resizeTable;
        for (Node<K, V> node : copyTable) {
            if (node != null) {
                do {
                   if (resizeTable[hash(node.key)] != null) {
                        Node<K, V> precededToInsert = resizeTable[hash(node.key)];
                        while (precededToInsert.next != null) {
                            precededToInsert = precededToInsert.next;
                        }
                        precededToInsert.next = node;
                    } else {
                        resizeTable[hash(node.key)] = put(node.key, node.value);
                    }
                } while ((node = node.next) != null);
            }
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

class Test {
    public static void main(String[] args) {
        MyMap<Integer, String> myMap = new MyHashMap<>();
        myMap.put(null, "node" + null);
        for (int i = 0; i < 100; i++) {
            myMap.put(i, "node" + i);
        }
    }
}