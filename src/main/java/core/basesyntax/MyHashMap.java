package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int hashCode = key == null ? 0 : key.hashCode();
        int index = (hashCode & Integer.MAX_VALUE) % table.length;
        if (table[index] == null) {
            table[index] = new Node<>(hashCode, key, value, null);
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key == null && key == null
                        || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            table[index] = new Node<>(hashCode, key, value, table[index]);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        int index = (hashCode & Integer.MAX_VALUE) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == null && key == null
                    || key != null && key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        int newCapacity = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                int index = (node.hash & Integer.MAX_VALUE) % newCapacity;
                Node<K, V> next = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
