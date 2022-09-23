package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void add(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int number = ((table.length - 2) & hash(key)) + 1;
        if (table[number] == null) {
            table[number] = newNode;
        } else {
            for (Node<K, V> node = table[number]; node != null; node = node.next) {
                if (key != null && key.equals(node.key)) {
                    node.value = value;
                    size--;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    break;
                }
            }
        }
    }

    private void addNullKey(V value) {
        Node<K, V> newNullKeyNode = new Node<>(0, null, value, null);
        if (table[0] == null) {
            table[0] = newNullKeyNode;
        } else {
            table[0].value = value;
            size--;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity * 2;
        table = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> tableNode : oldTable) {
            if (tableNode != null) {
                add(tableNode.key, tableNode.value);
            }
            if (tableNode != null && tableNode.next != null) {
                for (Node<K, V> node = tableNode.next; node != null; node = node.next) {
                    add(node.key, node.value);
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            addNullKey(value);
        } else {
            add(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        } else {
            int number = ((table.length - 2) & hash(key)) + 1;
            if (table[number] != null && table[number].next == null) {
                return table[number].value;
            } else {
                for (Node<K, V> node = table[number]; node != null; node = node.next) {
                    if (key.equals(node.key)) {
                        return node.value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
