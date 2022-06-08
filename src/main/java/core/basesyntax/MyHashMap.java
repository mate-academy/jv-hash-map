package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V> [] table;
    private int size;
    private final int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key);
        if (table[index] != null) {
            if (equalsKey(table[index].key, key)) {
                table[index].value = value;
                return;
            }
            if (hasNext(table[index])) {
                Node<K, V> current = table[index];
                while (hasNext(current)) {
                    current = current.next;
                    if (equalsKey(current.key, key)) {
                        current.value = value;
                        return;
                    }
                    if (!hasNext(current)) {
                        current.next = new Node<>(key, value, null);
                        size++;
                        return;
                    }
                }
            } else {
                table[index].next = new Node<>(key, value, null);
                size++;
            }
        } else {
            table[index] = new Node<>(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            } else if (hasNext(node)) {
                while (hasNext(node)) {
                    if (equalsKey(node.key, key)) {
                        return node.value;
                    }
                    node = node.next;
                }
            }
            if (equalsKey(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }


    private boolean hasNext(Node<K, V> node) {
        return node.next != null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private boolean equalsKey(K firstKey, K secondKey) {
        return firstKey == secondKey || firstKey != null && firstKey.equals(secondKey);
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] newArray = (Node<K, V>[]) new Node[table.length << 1];
            fillNewArray(newArray);
        }
    }

    private void fillNewArray(Node<K, V>[] newArray) {
        for (Node<K, V> newArrayElement : table) {
            if (newArrayElement == null) {
                continue;
            }
            if (hasNext(newArrayElement)) {
                Node<K, V> newNode = newArrayElement;
                while (hasNext(newNode)) {
                    newArray[hash(newNode.key)] = newArrayElement;
                    newNode = newNode.next;
                }
            } else {
                newArray[hash(newArrayElement.key)] = newArrayElement;
            }
        }
        table = newArray;
    }
}
