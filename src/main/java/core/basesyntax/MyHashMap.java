package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static int capacity;
    private static int size;
    private static int threshold;
    private static int index;
    private Node<K, V>[] table;
    private Node<K, V> currentNode;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    private static class Node<K, V> {
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
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key.hashCode(), key, value, null);
        if ((currentNode = getNode(key, table, capacity)) == null && ++size > threshold) {
            resize();
        } else if (currentNode != null){
            currentNode.value = value;
            return;
        }
        insertNode(newNode, table, capacity);
    }

    @Override
    public V getValue(K key) {
        return (currentNode = getNode(key, table, capacity)) == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode (K key, Node<K, V>[] tab, int cap) {
        index = getIndex(key, cap);
        currentNode = tab[index];
        if (currentNode == null) {
            return null;
        }
        while (!(key.equals(currentNode.key))) {
            if (currentNode.next == null) {
                return null;
            }
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private void insertNode (Node<K, V> entry, Node<K, V>[] tab, int cap) {
        index = getIndex(entry.key, cap);
        currentNode = tab[index];
        if (currentNode == null) {
            table[index] = entry;
            return;
        }
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = entry;
    }

    private int getIndex(K key, int cap) {
        return key.hashCode() % cap;
    }

    private void resize() {
        int newCap = capacity << 1;
        int newThr = threshold << 1;
        Node<K, V>[] newTable = new Node[newCap];
        transfer(newTable, newThr, newCap);
    }

    private void transfer(Node<K, V>[] newTable, int newThr, int newCap) {
        Node<K, V>[] oldTab = table;
        int elemLeft = size - 1;
        for (int i = 0; i < table.length; i++) {
            currentNode = oldTab[i];
            if (currentNode != null) {
               insertNode(currentNode, newTable, newCap);
               if (currentNode.next != null) {
                   oldTab[i] = currentNode.next;
                   i--;
               }
               elemLeft--;
               if (elemLeft == 0) {
                   capacity = newCap;
                   threshold = newThr;
                   table = newTable;
                   return;
               }
            }
        }
    }
}
