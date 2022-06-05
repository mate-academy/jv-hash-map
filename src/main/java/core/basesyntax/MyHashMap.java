package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    private void iterateNodes(Node<K,V> node, K key, V value) {
        boolean isNewNode = true;
        Node<K,V> prevNode = null;
        do {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
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
    @Override
    public void put(K key, V value) {
        isEmpty();
        int keyHash = key == null ? 0 : key.hashCode();
        int index = keyHash % table.length;
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);;
            size++;
        } else {
            iterateNodes(table[index], key, value);
        }
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Node<K, V> node : table) {
            if (node != null) {
                builder.append(node.key).append("=").append(node.value).append(", ");
            }
        }
        String result = builder.toString();
        return new StringBuilder(result.substring(0, result.length() - 2)).append("]").toString();
    }

    private void isEmpty() {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = key == null ? 0 : key.hashCode();
        }
    }
}
