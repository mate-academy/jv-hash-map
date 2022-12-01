package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);

    class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            for (Node<K,V> e = table[0]; e != null; e = e.next) {
                if (e.key == null) {
                    e.value = value;
                    return;
                }
            }
            addNode(0, null, value);
        } else {
            int index = getIndex(key);
            for (Node<K,V> e = table[index]; e != null; e = e.next) {
                if (e.key == key || e.key != null && e.key.equals(key)) {
                    e.value = value;
                    return;
                }
            }
            addNode(index, key, value);
        }
    }

    private void addNode(int index, K key, V value) {
        if (threshold < size + 1) {
            resize();
        }
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> e = table[index];
            table[index] = new Node<>(key, value, e);
        }
        size++;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private void transfer(Node[] newTable) {
        int newCapacity = newTable.length;
        for (Node<K,V> e : table) {
            while (null != e) {
                Node<K,V> next = e.next;
                int i = Math.abs(e.key.hashCode()) % newCapacity;
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    private V getForNullKey(K key) {
        for (Node<K,V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    private Node<K,V> getNode(K key) {
        for (Node<K,V> e = table[getIndex(key)]; e != null; e = e.next) {
            if (e.key == key || e.key != null && e.key.equals(key)) {
                return e;
            }
        }
        return null;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            getForNullKey(key);
        }
        Node<K,V> node = getNode(key);

        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }
}
