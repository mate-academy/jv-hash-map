package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int position = getPositionInTable(key);
        Node<K, V> newNode = new Node<>(getKeyHashCode(key), key, value, null);

        if (table[position] == null) {
            table[position] = newNode;

            size++;
        } else {
            Node<K, V> currElem = getElementByKeyOrLast(key, position);

            if ((key == null && currElem.key == null)
                            || (currElem.key != null && currElem.key.equals(key))
            ) {
                currElem.value = value;
            } else {
                currElem.next = newNode;

                size++;
            }
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int position = getPositionInTable(key);

        if (table[position] == null) {
            return null;
        }

        Node<K, V> currElem = getElementByKeyOrLast(key, position);

        return currElem.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getPositionInTable(K key) {
        return key == null ? 0 : getKeyHashCode(key) % table.length;
    }

    private Node<K, V> getElementByKeyOrLast(K key, int position) {
        Node<K, V> currElem = table[position];

        while (((currElem.key != null && !currElem.key.equals(key))
                || (currElem.key == null && key != null))
                && currElem.next != null
        ) {
            currElem = currElem.next;
        }

        return currElem;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] prevTable = table;

        table = new Node[table.length * CAPACITY_MULTIPLIER];
        size = 0;

        for (Node<K, V> node : prevTable) {
            if (node != null) {
                Node<K, V> currElem = node;

                while (currElem != null) {
                    put(currElem.key, currElem.value);

                    currElem = currElem.next;
                }
            }
        }
    }

    private int getKeyHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        int hash;
        K key;
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
