package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1 << 30;

    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);

    private MyNode<K, V>[] table;

    public MyHashMap() {
        table = (MyNode<K, V>[]) new MyNode[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        MyNode<K, V> node = table[index];

        if (node == null) {
            table[index] = new MyNode<>(key, value, key == null ? 0 : key.hashCode(), null);
            size++;
        } else {
            MyNode<K, V> current = node;
            while (true) {
                if ((key == null && current.key == null)
                        || (key != null && key.equals(current.key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new MyNode<>(key, value, key == null ? 0 : key.hashCode(), null);
            size++;
        }

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        MyNode<K, V> node = table[index];

        while (node != null) {
            if ((key == null && node.key == null)
                    || (key != null && key.equals(node.key))) {
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
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    public void resize() {
        if (capacity >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        final int oldCapacity = capacity;
        capacity = capacity * 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);

        MyNode<K, V>[] oldTable = table;
        table = (MyNode<K, V>[]) new MyNode[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            MyNode<K, V> current = oldTable[i];
            while (current != null) {
                MyNode<K, V> next = current.next;

                int newIndex = getIndex(current.key);

                current.next = table[newIndex];
                table[newIndex] = current;
                size++;

                current = next;
            }
        }
    }

    static class MyNode<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private MyNode<K, V> next;

        MyNode(K key, V value, int hash, MyNode<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
