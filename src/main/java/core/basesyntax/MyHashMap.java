package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        resizeIfReachesThreshold();
        int position = Math.abs(getIndex(key));
        Node<K, V> newNode = new Node<>(key, value, null);
        addToTable(newNode, position);
    }

    @Override
    public V getValue(K key) {
        int position = Math.abs(getIndex(key));
        Node<K, V> current = new Node<>(key, null, null);
        return getValueByKey(position, current);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROW_FACTOR];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    private void addToTable(Node<K, V> currentNode, int position) {
        Node<K, V> oldNode = table[position];
        if (oldNode == null) {
            table[position] = currentNode;
            size++;
            return;
        }
        while (oldNode.nextNode != null) {
            if (oldNode.equals(currentNode)) {
                oldNode.value = currentNode.value;
                return;
            }
            oldNode = oldNode.nextNode;
        }
        if (oldNode.equals(currentNode)) {
            oldNode.value = currentNode.value;
            return;
        }
        oldNode.nextNode = currentNode;
        size++;
    }

    private V getValueByKey(int position, Node<K, V> lookForNode) {
        Node<K, V> oldNode = table[position];
        while (oldNode != null) {
            if (oldNode.key == lookForNode.key || oldNode.equals(lookForNode)) {
                return oldNode.value;
            }
            oldNode = oldNode.nextNode;
        }
        return null;
    }

    private void resizeIfReachesThreshold() {
        if (threshold <= size) {
            resize();
        }
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!o.getClass().equals(this.getClass())) {
                return false;
            }
            Node<K, V> node = ((Node<K, V>) o);
            if (node.key == null && key == null || (node.key != null
                    && key != null && key.equals(node.key))) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = result * prime + (key == null ? 0 : key.hashCode());
            result = result * prime + (value == null ? 0 : value.hashCode());
            return result;
        }
    }
}
