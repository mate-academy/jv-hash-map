package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (threshold() == size) {
            resize();
        }
        int hash = key == null ? 0 : key.hashCode();
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        linkToBucket(table, newNode);
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int hash = key == null ? 0 : key.hashCode();
        int position = hash % table.length;
        position = validatePosition(position, table);
        Node<K, V> current = new Node<>(hash, key, null, null);
        return findNode(position, current);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * GROW_FACTOR];
        size = 0;
        copyElements(table, newTable);
        table = newTable;
    }

    private void copyElements(Node<K, V>[] table, Node<K, V>[] newTable) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                linkToBucket(newTable, currentNode);
                currentNode = currentNode.nextNode;
            }
        }
    }

    private void linkToBucket(Node<K, V>[] table, Node<K, V> currentNode) {
        int hash = currentNode.key == null ? 0 : currentNode.key.hashCode();
        int position = hash % table.length;
        position = validatePosition(position, table);
        addToTable(currentNode, position, table);
    }

    private void addToTable(Node<K, V> currentNode, int position,
                            Node<K, V>[] table) {

        Node<K, V> oldNode = table[position];
        Node<K, V> newCurrentNode = new Node<>(currentNode.hashcode, currentNode.key,
                currentNode.value, null);
        if (oldNode == null) {
            table[position] = newCurrentNode;
            size++;
            return;
        }
        if (oldNode != null && oldNode.equals(currentNode)) {
            oldNode.value = currentNode.value;
            return;
        }
        while (oldNode.nextNode != null) {
            if (oldNode.equals(currentNode)) {
                oldNode.value = currentNode.value;
                return;
            }
            oldNode = oldNode.nextNode;
        }
        oldNode.nextNode = newCurrentNode;
        size++;
    }

    private int validatePosition(int position, Node<K, V>[] table) {
        if (position > table.length || position < 0) {
            position = 0;
        }
        return position;
    }

    private V findNode(int position, Node<K, V> lookForNode) {
        Node<K, V> oldNode = table[position];
        if (oldNode == null) {
            return null;
        }
        while (oldNode != null) {
            if (oldNode.key == lookForNode.key || oldNode.equals(lookForNode)) {
                return oldNode.value;
            }
            oldNode = oldNode.nextNode;
        }
        return null;
    }

    private int threshold() {
        return (int) (table.length * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private int hashcode;
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(int hashcode, K key, V value, Node<K, V> nextNode) {
            this.hashcode = hashcode;
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
