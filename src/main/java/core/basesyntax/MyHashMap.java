package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_INCREASE_INDEX = 2;
    private int capacity;
    private int treshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            capacity = DEFAULT_INITIAL_CAPACITY;
            treshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
        }
        if (size >= treshold) {
            resize();
        }
        int indexOfKey = generateIndex(hash(key), capacity);
        Node<K,V> current = table[indexOfKey];
        while (current != null) {
            if ((key == null && current.key == null)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K,V> newVal = new Node<>(hash(key), key, value, null);
        newVal.next = table[indexOfKey];
        table[indexOfKey] = newVal;
        size++;
    }

    private void resize() {
        int oldCapacity = capacity;

        if (oldCapacity == Integer.MAX_VALUE) {
            treshold = Integer.MAX_VALUE;
            return;
        }

        Node<K,V>[] newTable = new Node[oldCapacity * DEFAULT_INCREASE_INDEX];
        transfer(newTable);
        table = newTable;
        capacity = oldCapacity * DEFAULT_INCREASE_INDEX;
        treshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
    }

    private void transfer(Node<K,V>[] newTable) {
        Node<K,V>[] sourse = table;
        int newCapacity = newTable.length;
        for (int i = 0; i < sourse.length; i++) {
            Node<K,V> current = sourse[i];
            if (current != null) {
                sourse[i] = null;
                while (current != null) {
                    Node<K,V> next = current.next;
                    int newIndex = generateIndex(hash(current.key), newCapacity);
                    current.next = newTable[newIndex];
                    newTable[newIndex] = current;
                    current = next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            Node<K,V> searshedNode = table[generateIndex(hash(key), capacity)];
            return searshedNode == null ? null : getNode(searshedNode, key);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private V getNode(Node<K,V> current, K key) {
        while (current != null) {
            if ((key == null && current.key == null)
                    || (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private int generateIndex(int hash, int capacity) {
        return Math.abs(hash % capacity);
    }

    private int hash(K o) {
        return (o == null) ? 0 : o.hashCode();
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node() {

        }

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
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

        public String toString() {
            return key + "=" + value;
        }

        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null && getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) o;
            return key.equals(node.key) && value.equals(node.value);
        }
    }
}
