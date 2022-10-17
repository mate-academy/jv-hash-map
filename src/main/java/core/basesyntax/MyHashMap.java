package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            table[0] = new Node<>(hash(key), key, value, null);
            size++;
        } else if (containsKey(key)) {
            Node<K,V> currentNode = getNode(key);
            currentNode.value = value;
        } else {
            int index = hash(key) % table.length;
            if (table[index] == null) {
                table[index] = new Node<>(hash(key), key, value, null);
            } else {
                Node<K, V> current = table[index];
                while (current.next != null) {
                    current = current.next;
                }
                current.next = new Node<>(hash(key), key, value, null);
            }
            size++;
        }
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNode(key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    private int hash(Object key) {
        int h;
        if (key == null) {
            return 0;
        } else {
            h = key.hashCode();
            if (h < 0) {
                h = h * (-1);
            }
            return h;
        }
    }

    private Node<K, V> getNode(K key) {
        if (table != null) {
            Node<K, V> currentNode;
            for (int i = 0; i < table.length; i++) {
                currentNode = table[i];
                while (currentNode != null) {
                    if (currentNode.key == key
                            || currentNode.key != null && currentNode.key.equals(key)) {
                        return currentNode;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    private void resize() {
        Node[] oldTab = table;
        table = new Node[oldTab.length << 1];
        size = 0;
        threshold = threshold << 1;
        transition(oldTab);
    }

    private void transition(Node[] oldTab) {
        for (int i = 0; i < oldTab.length; i++) {
            Node<K, V> curr = oldTab[i];
            while (curr != null) {
                put(curr.key, curr.value);
                curr = curr.next;
            }
        }
    }
}
