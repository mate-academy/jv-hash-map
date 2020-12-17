package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_INCREASE_CAPACITY = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private int size;
    private float threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return Math.abs(key == null ? 0 : (key.hashCode()));
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V>[] tab = table;
        int index = getHash(key) % tab.length;
        int hash = getHash(key);
        if (tab[index] == null) {
            tab[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> temp = tab[index];
            if (key == temp.key || (key != null && key.equals(temp.key))) {
                temp.value = value;
            } else {
                while (temp.next != null) {
                    temp = temp.next;
                    if (key == temp.key || (key != null && key.equals(temp.key))) {
                        temp.value = value;
                        return;
                    }
                }
                Node<K, V> newNode = new Node<>(hash, key, value, null);
                temp.next = newNode;
                size++;
            }
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[oldTable.length * DEFAULT_INCREASE_CAPACITY];
        threshold = table.length * DEFAULT_LOAD_FACTOR;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> nodeOld = oldTable[i];
            while (nodeOld != null) {
                put(nodeOld.key, nodeOld.value);
                nodeOld = nodeOld.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> temp = table[getHash(key) % table.length];
        while (temp != null) {
            if (key == temp.key || key != null && key.equals(temp.key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
