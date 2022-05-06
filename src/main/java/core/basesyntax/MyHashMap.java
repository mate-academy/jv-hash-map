package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity = 16;
    private MyNode<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new MyNode[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = getIndex(key);
        MyNode<K, V> node = new MyNode<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = node;
            size++;
        } else {
            MyNode<K, V> current = table[hash];
            MyNode<K, V> previous = null;
            while (current != null) {
                if ((current.key == null && key == null) || (current.key != null && current.key.equals(key))) {
                    current.value = node.value;
                    return;
                }
                previous = current;
                current = current.next;
            }
            previous.next = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int hash = getIndex(key);
        if (hash == 0) {
            value = table[0].getValue();
        } else {
            MyNode<K, V> node = table[hash];
            while (node != null) {
                if (node.getKey().equals(key)) {
                    value = node.getValue();
                    break;
                }
                node = node.next;
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : key.hashCode() % capacity;
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
