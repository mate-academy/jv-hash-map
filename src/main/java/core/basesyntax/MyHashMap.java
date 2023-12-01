package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existedNode = getNode(key);
        if (existedNode != null) {
            existedNode.value = value;
            return;
        }
        int index = getIndex(key);
        table[index] = new Node<>(key, value, table[index]);
        size++;
        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        return getNode(key) == null ? null : getNode(key).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key == null && key == null)
                    || (node.key != null && node.key.equals(key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
