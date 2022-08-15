package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(getIndex(key), key, value,null);
        if (table[node.hash] == null) {
            table[node.hash] = node;
            size++;
        } else {
            Node<K, V> newNode = table[node.hash];
            do {
                if (key == newNode.key || newNode.key != null && newNode.key.equals(key)) {
                    newNode.value = node.value;
                    break;
                }
                if (newNode.next == null) {
                    newNode.next = node;
                    size++;
                    break;
                }
                newNode = newNode.next;

            } while (newNode != null);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] oldTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K,V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }
}
