package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newEntry = new Node<>(key, value, null);
        if (table[index] != null) {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                boolean isSameNullKey = (key == null && currentNode.key == null);
                boolean isSameKey = currentNode.key != null && currentNode.key.equals(key);
                int cases = isSameNullKey ? 1 : isSameKey ? 2 : 3;
                switch (cases) {
                    case (1):
                        currentNode.value = value;
                        return;
                    case (2):
                        newEntry.next = currentNode.next;
                        if (previousNode == null) {
                            table[index] = newEntry;
                        } else {
                            previousNode.next = newEntry;
                        }
                        return;
                    default:
                        previousNode = currentNode;
                        currentNode = currentNode.next;
                }
            }
            previousNode.next = newEntry;
        } else {
            table[index] = newEntry;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = getIndex(key);
        if (table != null) {
            Node<K, V> temp = table[hash];
            while (temp != null) {
                boolean isSameNullKey = key == null && temp.key == null;
                boolean isSameKey = temp.key != null && temp.key.equals(key);
                if (isSameNullKey || isSameKey) {
                    return temp.value;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    public void putAll(Node<K, V>[] nodes) {
        for (Node<K, V> node : nodes) {
            while (node != null) {
                K key = node.key;
                V value = node.value;
                put(key, value);
                size--;
                if (node.next != null) {
                    node = node.next;
                    K key1 = node.key;
                    V value1 = node.value;
                    put(key1, value1);
                    size--;
                }
                node = node.next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if (size > threshold) {
            Node<K, V>[] oldTab = table;
            int oldCap = oldTab.length;
            int newCap;
            if ((newCap = oldCap * 2) < 10000 && oldCap >= DEFAULT_INITIAL_CAPACITY) {
                threshold = threshold * 2;
            }
            table = new Node[newCap];
            putAll(oldTab);
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
