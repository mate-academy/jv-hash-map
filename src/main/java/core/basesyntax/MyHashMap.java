package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        int place = Math.abs(hash(key)) % table.length;
        Node<K,V> current = table[place];
        if (current != null) {
            while (current != null) {
                if (current.key == key || current.key != null && current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void putVal(int hash, K key, V value) {
        Node<K,V> current = table[Math.abs(hash) % table.length];
        Node<K,V> newNode = new Node<>(hash, key, value, null);
        if (key == null) {
            while (current != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
        }
        if (current != null) {
            while (current != null) {
                if (current.key == key || current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
        }
        if (table[Math.abs(hash) % table.length] == null) {
            table[Math.abs(hash) % table.length] = newNode;
        } else {
            current = table[Math.abs(hash) % table.length];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        resize();
    }

    private void resize() {
        if (size > table.length * LOAD_FACTOR) {
            int newCapacity = table.length << 1;
            Node<K,V>[] newTab = new Node[newCapacity];
            Node<K,V> temp;
            for (Node<K,V> current : table) {
                if (current != null) {
                    while (current != null) {
                        Node<K,V> newNode =
                                new Node<>(current.hash, current.key, current.value, null);
                        if (newTab[Math.abs(current.hash) % newTab.length] == null) {
                            newTab[Math.abs(current.hash) % newTab.length] = newNode;
                        } else {
                            temp = newTab[Math.abs(current.hash) % newTab.length];
                            while (temp.next != null) {
                                temp = temp.next;
                            }
                            temp.next = newNode;
                        }
                        current = current.next;
                    }
                }
            }
            table = newTab;
        }
    }

    private static class Node<K,V> {
        private int hash;
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
