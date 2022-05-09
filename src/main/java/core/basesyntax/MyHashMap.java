package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE = 2;
    private MyNode<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new MyNode[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (threshold == size) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int hash = getIndex(key);
        MyNode<K, V> node = table[hash];
        while (node != null) {
            if ((node.key == null && key == null)
                    || (node.key != null && node.key.equals(key))) {
                value = node.getValue();
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
        return (key == null) ? 0 : Math.abs(key.hashCode() % DEFAULT_CAPACITY);
    }

    private void putValue(K key, V value) {
        int hash = getIndex(key);
        MyNode<K, V> node = new MyNode<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = node;
        } else {
            MyNode<K, V> current = table[hash];
            MyNode<K, V> previous = null;
            while (current != null) {
                if ((current.key == null && key == null)
                        || (current.key != null && current.key.equals(key))) {
                    current.value = node.value;
                    return;
                }
                previous = current;
                current = current.next;
            }
            previous.next = node;
        }
        size++;
    }

    private void resize() {
        threshold = threshold * RESIZE;
        size = 0;
        MyNode<K, V>[] oldHashTable = table;
        table = new MyNode[oldHashTable.length * RESIZE];
        for (MyNode<K, V> node : oldHashTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class MyNode<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private MyNode<K, V> next;

        MyNode(int hash, K key, V value, MyNode<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public MyNode<K, V> getNext() {
            return next;
        }

        public void setNext(MyNode<K, V> next) {
            this.next = next;
        }

    }
}
