package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] map; // наш масив
    private int size = 0; // кількість елементів
    private double threshold; // значення критичного заповненення масиву

    public MyHashMap() {
        map = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    private static class Node<K,V> {
        V value;
        MyHashMap.Node<K,V> next;
        private int hash;
        private K key;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();// збільшуємо масив
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key); // знаходимо індекс
        if (map[index] == null) {
            map[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = map[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getIndex(key);
        Node<K, V> node = map[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] mapOld = map;
        map = (Node<K, V>[]) new Node[(int) (map.length * 2)];
        threshold = map.length * DEFAULT_LOAD_FACTOR;
        for (Node<K, V> kvNode : mapOld) {
            Node<K, V> nodeOld = kvNode;
            while (nodeOld != null) {
                put(nodeOld.key, nodeOld.value);
                nodeOld = nodeOld.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % map.length);
    }
}

