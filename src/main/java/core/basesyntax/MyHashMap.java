package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_LENGTH];
    }

    private class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = nodeHash(key);
        }

        private int nodeHash(K key) {
            return key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        resize();
        if (table[node.hash % table.length] != null) {
            Node<K, V> currentNode = table[node.hash % table.length];
            Node<K, V> prevNode = currentNode;
            if (key == currentNode.key || key!= null && key.equals(currentNode.key)) {
                node.next = currentNode.next;
                table[node.hash % table.length] = node;
                return;
            }
            while (currentNode != null) {
                if (key == currentNode.key || key!= null && key.equals(currentNode.key)) {
                    node.next = currentNode.next;
                    prevNode.next = node;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;

            }
            prevNode.next = node;
        } else {
            table[node.hash % table.length] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[(key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() : key.hashCode()) % table.length];
        while (node != null) {
            if (key == node.key || key!= null && key.equals(node.key)) {
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
        float threshold = table.length * LOAD_FACTOR;
        if (size == threshold) {
            Node<K, V>[] oldTab = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> node : oldTab) {
                if (node != null) {
                    Node<K, V> currentNode = node;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }
}
