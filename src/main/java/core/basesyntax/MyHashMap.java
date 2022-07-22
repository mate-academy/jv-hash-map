package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V> [] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K,V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (equalNodes(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (equalNodes(node.key, key)) {
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V> [] tableCopy = table;
        table = new Node[(table == null) ? DEFAULT_INITIAL_CAPACITY : table.length << 1];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (tableCopy == null) {
            return;
        }
        size = 0;
        for (Node<K, V> node : tableCopy) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean equalNodes(K firstKey, K secondKey) {
        return (firstKey == null & secondKey == null)
                || (firstKey != null & secondKey != null && secondKey.equals(firstKey));
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

