package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_fACTOR = 0.75f;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_fACTOR);
    private int size;

    @Override
    public void put(K key, V value) {
        resize();
        add(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        for (Node<K, V> var = table[index]; var != null; var = var.next) {
            if (key == var.key
                    || key != null && key.equals(var.key)) {
                return var.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] tempTable = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> nodeBucket : tempTable) {
                if (nodeBucket != null) {
                    for (Node<K, V> temp = nodeBucket; temp != null; temp = temp.next) {
                        add(temp.key, temp.value);
                    }
                }
            }
            threshold = (int) (table.length * LOAD_fACTOR);
        }
    }

    private void add(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(key, table.length);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            for (Node<K, V> var = table[index]; var != null; var = var.next) {
                if (key == var.key
                        || key != null && key.equals(var.key)) {
                    var.value = value;
                    break;
                }
                if (var.next == null) {
                    var.next = node;
                    size++;
                    break;
                }
            }
        }
    }

    private int getIndex(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
