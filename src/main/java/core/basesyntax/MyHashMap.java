package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private Node<K, V>[] table;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int currentHash = getHash(key);
        int index = setIndex(currentHash);
        Node<K, V> node = table[index];
        if (node != null) {
            Node<K, V> prev = node;
            while (node != null) {
                if (key == node.key) {
                    node.value = value;
                    return;
                }
                if (key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                prev = node;
                node = node.next;
            }
            prev.next = new Node<>(key, value);
            ++size;
            return;
        }
        table[index] = new Node<>(key, value);
        ++size;
    }

    @Override
    public V getValue(K key) {
        return get(key);
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean containsKey(K key) {
        int index = setIndex(getHash(key));
        Node<K, V> node = table[index];
        if (node == null) {
            return false;
        }
        while (node != null) {
            if (key == node.key || node.key.equals(key)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    private static int getHash(Object object) {
        return object == null ? 0 : object.hashCode() >> 31 ^ object.hashCode();
    }

    private int setIndex(int hash) {
        return hash % this.capacity;
    }

    private V get(K key) {
        int currentHash = getHash(key);
        int index = setIndex(currentHash);
        Node<K, V> node = table[index];
        if (node != null) {
            do {
                if (key == node.key) {
                    return node.value;
                }
                if (key != null && key.equals(node.key)) {
                    return node.value;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }

    private void resize() {
    }

    private Node<K, V> transfer() {
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

class T {
    public static void main(String[] args) {
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("Bogdan", "Hello");
        map.put("Ivan", "Hello");
        map.put("Dmytro", "Hello");
        map.put("Andrii", "Hello");
        map.put("Vasyl", "Hello");
        map.put("Petro", "Hello");
        map.put("Petro", "Hi");
        map.put("Vasyl", "Hi");
        map.put(null, "Hello");
        map.put(null, "Hi");
    }
}
