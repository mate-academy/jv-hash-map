package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float RESIZE_FACTOR = 0.75f;
    private int currentCapacity;
    private int size;
    private float loadFactor;
    private Node<K, V>[] table;
    private int threshold;
    private int newCap;
    private int newThr;

    public MyHashMap() {

    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0 || getSize() == threshold ) {
            table = resize();
        }
        Node newNode = new Node(hash(key), key, value, null);
        insert(newNode, table);
    }

    @Override
    public V getValue(K key) {
        if (this.table == null || table.length == 0) {
            return  null;
        }
        int index = hash(key) % table.length;
        if (table[index] == null && table.length != 0) {
            return null;
        }
        if (table[index].next == null) {
            return table[index].value;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key.equals(key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hash(K key) {
        int h = hashCode();
        if (h < 0 ) {
            h *= - 1;
        }
        return (key == null) ? 0 : h  ^ (h >>> 16);
    }

    public void insert(Node<K, V> node, Node<K, V>[] newTable) {
            node.next = null;
            int index = hash(node.key) % newCap;
            if (newTable[index] == null) {
                newTable[index] = node;
                size++;
            } else {
                Node<K, V> currentNode = newTable[index];
                while (currentNode != null) {
                    if (currentNode.key == null && node.key == null) {
                        currentNode.value = node.value;
                        break;
                    }
                    if (currentNode.key != null && currentNode.key.equals(node.key)) {
                        currentNode.value = node.value;
                        break;
                    }
                    if (currentNode.next == null) {
                        currentNode.next = node;
                        size++;
                        break;
                    }
                    currentNode = currentNode.next;
                }
            }
    }

    public Node<K, V>[] resize() {
        Node<K, V>[] oldTb = table;
        int oldCap = oldTb == null ? 0 : oldTb.length;
        if (oldCap == 0) {
            newCap = DEFAULT_CAPACITY;
            threshold = (int)(RESIZE_FACTOR * newCap);
            Node<K, V>[] newTable = new Node[newCap];
            return newTable;
        } else {
            newCap = oldCap * 2;
            threshold = (int) (RESIZE_FACTOR * newCap);
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCap];
            for (Node<K, V> node : oldTb) {
                if (node != null) {
                    while (node != null) {
                        insert(node , newTable);
                        node = node.next;
                    }
                }
            }
            return newTable;
        }
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;

        }

    }


}
