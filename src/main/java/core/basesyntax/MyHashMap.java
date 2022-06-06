package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            iterateNodes(table[index], key, value);
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K,V> node = table[index];
        if (node != null) {
            do {
                if (key == node.key || key != null && key.equals(node.key)) {
                    return node.value;
                } else {
                    node = node.next;
                }
            } while (node != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            int newCapacity = table.length << 1;
            threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
            Node<K,V>[] oldTable = table;
            table = new Node[newCapacity];
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    while (node != null) {
                        put(node.key, node.value);
                        node = node.next;
                    }
                }
            }
        }
    }

    private int hash(K key) {
        int keyHash = key == null ? 0 : key.hashCode();
        int index = Math.abs(keyHash % table.length);
        return index;
    }

    private void iterateNodes(Node<K,V> node, K key, V value) {
        boolean isNewNode = true;
        Node<K,V> prevNode = null;
        do {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                node.value = value;
                isNewNode = false;
                break;
            } else {
                prevNode = node;
                node = node.next;
            }
        } while (node != null);
        if (isNewNode) {
            prevNode.next = new Node<>(key, value, null);
            size++;
        }
    }

    private class Node<K, V> {

        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
