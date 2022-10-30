package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndexForKey(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (current.key != null && current.key.equals(key) || current.key == key) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key != null && current.key.equals(key) || current.key == key) {
                current.value = value;
                return;
            }
            current.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey(key);
        }
        Node<K, V> node = table[getIndexForKey(key)]; //
        if (node == null) {
            return null;
        } else {
            Node<K, V> current = node;
            while (current.next != null) {
                if (current.key != null && current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
            return current.value;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexForKey(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private V getForNullKey(K key) {
        if (size == 0) {
            return null;
        }
        for (Node<K, V> curr = table[0]; curr != null; curr = curr.next) {
            if (curr.key == null) {
                return curr.value;
            }
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] oldMap = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> kvNode : oldMap) {
            if (kvNode != null) {
                Node<K, V> current = kvNode;
                while (current.next != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
                put(current.key, current.value);
            }
        }
    }

    static class Node<K, V> {
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
