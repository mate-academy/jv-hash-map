package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_fACTOR = 0.75f;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_fACTOR);
    private int size;

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(getHash(key), key, value, null);
        int index = getBucket(key, table.length);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            for (Node<K, V> var = table[index]; var != null; var = var.next) {
                if (key == var.key
                        || getHash(key) == var.hash && key != null && key.equals(var.key)) {
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

    @Override
    public V getValue(K key) {
        int index = getBucket(key, table.length);
        for (Node<K, V> var = table[index]; var != null; var = var.next) {
            if (key == var.key
                    || getHash(key) == var.hash && key != null && key.equals(var.key)) {
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
            for (Node<K, V> nodeBucket : tempTable) {
                if (nodeBucket != null) {
                    for (Node<K, V> temp = nodeBucket; temp != null; temp = temp.next) {
                        int index = getBucket(temp.key, table.length);
                        Node<K, V> node = new Node<>(temp.hash, temp.key, temp.value, null);
                        if (table[index] == null) {
                            table[index] = node;
                        } else {
                            for (Node<K, V> var = table[index]; var != null; var = var.next) {
                                if (var.next == null) {
                                    var.next = node;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            threshold = (int) (table.length * LOAD_fACTOR);
        }
    }

    private int getBucket(K key, int length) {
        return Math.abs(getHash(key) % length);
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
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
