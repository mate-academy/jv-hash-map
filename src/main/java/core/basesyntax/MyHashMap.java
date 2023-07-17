package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] map;

    public MyHashMap() {
        this.map = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node newNode = new Node<>(key == null ? 0 : key.hashCode(), key, value, null);
        if (map[getIndex(key)] != null) {
            Node oldNode = map[index];
            while (true) {
                if (oldNode.key == key || oldNode.key != null && oldNode.key.equals(key)) {
                    oldNode.value = value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    resize();
                    return;
                }
                oldNode = oldNode.next;
            }
        } else {
            map[index] = newNode;
            size++;
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node oldNode = map[getIndex(key)];
        if (oldNode == null) {
            return null;
        }
        while (true) {
            if (oldNode.key == key || oldNode.key != null && oldNode.key.equals(key)) {
                return (V) oldNode.value;
            } else {
                if (oldNode.next == null) {
                    return null;
                } else {
                    oldNode = oldNode.next;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (map.length * DEFAULT_LOAD_FACTOR == size) {
            Node<K, V>[] oldMap = map;
            map = new Node[oldMap.length * 2];
            size = 0;
            for (Node<K, V> element : oldMap) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    final int getIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode() % map.length);
        }
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
