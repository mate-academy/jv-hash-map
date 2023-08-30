package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MULTIPLY_CAPACITY = 2;
    private Node<K, V>[] table;
    private float threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = indexBucket(key);
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
        } else {
            // в цьому бакеті щось є(пробігтись, перевірячи ключі,
            // якщо == міняємо, якщо колізія - некст
            Node<K, V> prev = node;
            while (node != null) {
                if (key == node.key || (key != null && key.equals(node.key))) {
                    node.value = value;
                    return;
                }
                prev = node;//1  2  3 null
                node = node.next;
            }
            prev.next = newNode;
        }
        size++;
        // пошук потрібного бакету
        // чи бакет емпті, якщо так - шукаємо до кінця списку
    }

    @Override
    public V getValue(K key) {
        int index = indexBucket(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
        // bucket
        // проходжусь по  списку і порівнюю ключі (якщо потрібке ключ - ретурн, якщо ні - null
        // getHasshcode
        // keep track of getbucket

    }

    @Override
    public int getSize() {
        return size;
    }

    public int indexBucket(K key) {
        return key == null ? 0 : hash(key) % 16;
    }

    public int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    public void resize() {
        int newCapacity = table.length * MULTIPLY_CAPACITY;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexBucket(node.key);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = DEFAULT_LOAD_FACTOR * newCapacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
