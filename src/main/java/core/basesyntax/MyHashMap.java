package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (table == null || size == 0) {
            resize();
        }
        int index = hash(key) % table.length;
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == key
                        || (currentNode.key != null && currentNode.key.equals(key))) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash(key), key, value, null);
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        } else {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int index = hash(key) % table.length;
            if (table[index] != null) {
                Node<K, V> currentNode = table[index];
                while (currentNode != null) {
                    if (currentNode.key == key
                            || (currentNode.key != null && currentNode.key.equals(key))) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        if (table == null || size == 0) {
            table = new Node[INITIAL_CAPACITY];
            threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        } else {
            threshold = threshold << 1;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    putWhileResizing(node.key, node.value);
                    Node<K, V> currentNode = node.next;
                    while (currentNode != null) {
                        putWhileResizing(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private void putWhileResizing(K key, V value) {
        int index = hash(key) % table.length;
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == key
                        || (currentNode.key != null && currentNode.key.equals(key))) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash(key), key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        } else {
            table[index] = new Node<>(hash(key), key, value, null);
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
