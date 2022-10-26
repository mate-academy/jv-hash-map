package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexForKey(key);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = table[index];
            while (node != null) {
                K checkKye = node.key;
                if (checkKye == key || checkKye != null && checkKye.equals(key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    break;
                }
                node = node.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexForKey(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
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

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    private void resize() {
        int newCap = table.length << 1;
        threshold = threshold << 1;
        size = 0;
        Node<K, V>[] newTab = new Node[newCap];
        final Node<K, V>[] oldTabe = table;
        table = newTab;

        for (Node<K, V> node : oldTabe) {
            while (node != null) {
                Node<K, V> next = node.next;
                node.next = null;
                put(node.key, node.value);
                node = next;
            }
        }
    }

    private int getIndexForKey(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return hash & (table.length - 1);
    }

}
