package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int RESIZING_MULTIPLIER = 2;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    public class Node<K, V> {
        private K key;
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
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> targetNode = table[index];
        if (targetNode == null) {
            table[index] = new Node(key, value, null);
            size++;
        } else {
            while (targetNode != null) {
                if (targetNode.key == key
                        || key != null && key.equals(targetNode.key)) {
                    targetNode.value = value;
                    return;
                }
                if (targetNode.next == null) {
                    targetNode.next = new Node(key, value, null);
                    size++;
                    return;
                }
                targetNode = targetNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> searcherNode = table[index];
        while (searcherNode != null) {
            if (searcherNode.key == key
                    || key != null && key.equals(searcherNode.key)) {
                return searcherNode.value;
            }
            searcherNode = searcherNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(Object key) {
        return (key == null) ? 0 : Math.abs((key.hashCode()) % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * RESIZING_MULTIPLIER];
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K,V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> targetNode = oldTable[i];
            while (targetNode != null) {
                put(targetNode.key, targetNode.value);
                targetNode = targetNode.next;
            }
        }
    }
}
