package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V> [] map;
    private int size = 0;

    public MyHashMap() {
        map = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        if (size >= (map.length * LOAD_FACTOR)) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> node = map[hash];

        if (node == null) {
            map[hash] = new Node<>(hash, key, value, null);
        } else {
            while (node != null) {
                if (key == node.key || (key != null && key.equals(node.key))) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
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
        Node<K, V> node = map[hash(key)];
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
        map = new Node[oldMap.length * 2];
        for (Node<K, V> node : oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private int hash;
        private K key;
        private V value;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % map.length;
    }

}
