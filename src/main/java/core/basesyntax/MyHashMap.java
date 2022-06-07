package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V> [] table;
    private int size;

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        resize();
        int el = hash(key) % table.length;
        if (table[el] != null) {
            if (table[el].key == key || table[el].key != null && table[el].key.equals(key)) {
                table[el].value = value;
                return;
            }
            if (table[el].next != null) {
                Node<K, V> current = table[el];
                while (current.next != null) {
                    if (current.key == key || current.key != null && current.key.equals(key)) {
                        current.value = value;
                        return;
                    }
                    current = current.next;
                    if (current.next == null) {
                        current.next = new Node<>(hash, key, value, null);
                        size++;
                        return;
                    }
                }
            } else {
                table[el].next = new Node<>(hash, key, value, null);
                size++;
            }
        } else {
            table[el] = new Node<>(hash, key, value, null);
            size++;
        }
    }

    private void resize() {
        if (table == null) {
            table = new Node[DEFAULT_CAPACITY];
        } else {
            Node<K, V>[] newArray = new Node[table.length << 1];
            int oldCap = table.length;
            int thresold = (int) (oldCap * LOAD_FACTOR);
            if (size + 1 > thresold) {
                for (Node<K, V> newArrayElement : table) {
                    if (newArrayElement == null) {
                        continue;
                    }
                    if (newArrayElement.next != null) {
                        while (newArrayElement.next != null) {
                            newArray[hash(newArrayElement.key) % table.length] = newArrayElement;
                            newArrayElement = newArrayElement.next;
                        }
                    } else {
                        newArray[hash(newArrayElement.key) % table.length] = newArrayElement;
                    }
                }
                table = newArray;
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() < 0 ? (key.hashCode() * -1) : key.hashCode();
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            }
            if (node.next != null) {
                while (node.next != null) {

                    if (node.key == key || node.key != null && node.key.equals(key)) {
                        return node.value;
                    }
                    node = node.next;
                }
            }
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            }

        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
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
}
