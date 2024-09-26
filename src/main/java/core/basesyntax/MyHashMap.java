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

            if ((key == null && currElem.getKey() == null)
                            || (currElem.getKey() != null && currElem.getKey().equals(key))
            ) {
                currElem.setValue(value);
            } else {
                currElem.setNext(newNode);

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

        return currElem.getValue();
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

        while (((currElem.getKey() != null && !currElem.getKey().equals(key))
                || (currElem.getKey() == null && key != null))
                && currElem.getNext() != null
        ) {
            currElem = currElem.getNext();
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
                    put(currElem.getKey(), currElem.getValue());

                    currElem = currElem.getNext();
                }
            }
        }
    }

    private int getKeyHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
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

        public int getHash() {
            return hash;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
