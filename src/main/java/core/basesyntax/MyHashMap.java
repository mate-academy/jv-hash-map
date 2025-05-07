package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        capacity = table.length;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (shouldResize()) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        }
        Node<K, V> previousNode = null;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (compareKeys(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        if (previousNode != null) {
            previousNode.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        V value = null;
        Node<K, V> node = table[index];
        while (node != null) {
            if (compareKeys(node.key, key)) {
                value = node.value;
                break;
            }
            node = node.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null)
                ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean compareKeys(K firstKey, K secondKey) {
        return firstKey == secondKey
                || (firstKey != null
                && firstKey.equals(secondKey));
    }

    private boolean shouldResize() {
        return this.size
                > Math.ceil((double) this.capacity
                * LOAD_FACTOR);
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] oldTab = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTab) {
            Node<K, V> temp = node;
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object element) {
            if (element == this) {
                return true;
            }
            if (element == null) {
                return false;
            }

            if (element.getClass().equals(Node.class)) {
                Node current = (Node) element;
                return (this.key == null && current.key == null)
                        || (this.key != null && this.key.equals(current.key))
                        && (this.value == null && current.value == null)
                        || (this.value != null && this.value.equals(current.value))
                        && (this.next == null && current.next == null)
                        || (this.next != null && this.next.equals(current.next));
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result
                    + ((key == null)
                    ? 0 : key.hashCode());
            result = 31 * result
                    + ((value == null)
                    ? 0 : value.hashCode());
            result = 31 * result
                    + ((next == null)
                    ? 0 : next.hashCode());
            return result;
        }
    }
}
