package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        Node<K, V> data = new Node<>(key, value, null);

        boolean increaseSize = putData(data);

        if (increaseSize && ++size > (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key)];

        if (node != null) {
            do {
                if (node.key == key
                        || (node.key != null
                        && node.key.equals(key))) {
                    return node.value;
                }

                node = node.next;
            } while (node != null);
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean putData(Node<K, V> data) {
        int hash = getHash(data.key);

        Node<K, V> nodeByHash = table[hash];

        if (nodeByHash == null) {
            table[hash] = data;
            return true;
        } else {
            return putNextOrChange(nodeByHash, data);
        }
    }

    private boolean putNextOrChange(Node<K, V> src, Node<K, V> element) {
        Node<K, V> curr = src;
        Node<K, V> next;
        K elementKey = element.key;

        do {
            next = curr.next;

            if (curr.key == elementKey
                    || (curr.key != null
                    && curr.key.equals(elementKey))) {
                curr.value = element.value;
                return false;
            }

            if (next == null) {
                curr.next = element;
            }

            curr = next;
        } while (next != null);

        return true;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        transferAllData(oldTable);
    }

    private void transferAllData(Node<K, V>[] src) {
        Node<K, V> next;
        for (Node<K, V> data : src) {
            while (data != null) {
                next = data.next;
                data.next = null;
                putData(data);
                data = next;
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
