package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int PLACE_FOR_NULL_KEY = 0;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;
    private Node<K, V> currentNode;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
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

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if ((currentNode = getNode(key, table, capacity)) == null
                && ++size > threshold) {
            resize();
        }
        else if (currentNode != null) {
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

    private Node<K, V> getNode(K key, Node<K, V>[] tab, int cap) {
        if (key == null) {
            currentNode = tab[PLACE_FOR_NULL_KEY];
        } else {
            currentNode = tab[getIndex(key, cap)];
        }
        if (currentNode == null) {
            return null;
        }
        while ((key == null) ? (currentNode.key != null) : !(key.equals(currentNode.key))) {
            if (currentNode.next == null) {
                return null;
            }
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private int getIndex(K key, int cap) {
        return Math.abs(key.hashCode()) % cap;
    }

    private void insertNode(Node<K, V> entry, Node<K, V>[] tab, int cap) {
        int index = (entry.key == null) ? PLACE_FOR_NULL_KEY : getIndex(entry.key, cap);
        currentNode = tab[index];
        if (currentNode == null) {
            tab[index] = entry;
            return;
        }
        while (currentNode.next != null) {
//            if ((entry.key == null) ? (currentNode.key == null) : (entry.key.equals(currentNode.key))) {
//                currentNode.value = entry.value;
//                return;
//            }
            currentNode = currentNode.next;
        }
        currentNode.next = entry;
    }

    private void resize() {
        int newCap = capacity << 1;
        int newThr = threshold << 1;
        Node<K, V>[] newTable = new Node[newCap];
        transfer(newTable, newThr, newCap);
    }

    private void transfer(Node<K, V>[] newTab, int newThr, int newCap) {
        Node<K, V>[] oldTab = table;
        Node<K, V> insertableNode;
        int elemLeft = size - 1;
        for (int i = 0; i < table.length; i++) {
            if (oldTab[i] != null) {
                    insertableNode = new Node<>(oldTab[i].key,
                            oldTab[i].value,
                            null);
                    insertNode(insertableNode, newTab, newCap);
                currentNode = oldTab[i];
                if (currentNode.next != null) {
                    oldTab[i] = currentNode.next;
                    i--;
                }
                elemLeft--;
                if (elemLeft == 0) {
                    capacity = newCap;
                    threshold = newThr;
                    table = newTab;
                    return;
                }
            }
        }
    }
}
