package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int RESIZE_VALUE = 2;

    private Node<K, V> [] map;
    private int size = 0;

    public MyHashMap() {
        map = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        if (size > (map.length * LOAD_FACTOR)) {
            resize();
        }
        int hash = getIndex(key);
        Node<K, V> node = map[hash];

        if (node == null) {
            map[hash] = newNode;
        } else {
            while (node != null) {
                if (key == node.key || (key != null && key.equals(node.key))) {
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
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = map[getIndex(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
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
        Node<K, V>[] oldMap = map;
        map = new Node[oldMap.length * RESIZE_VALUE];
        for (Node<K, V> node : oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % map.length;
    }
}
