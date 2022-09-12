package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKeys(value);
        } else {
            int index = indexForPosition(findingHash(key.hashCode()), table.length);
            Node<K, V> newNode = new Node<>(findingHash(key.hashCode()), key, value, null);
            if (index == 0 && table[index] != null) {
                Node<K, V> oldNode = table[index];
                while (oldNode.next != null) {
                    oldNode = oldNode.next;
                }
                oldNode.setNext(newNode);
                if (++size > threshold) {
                    resize();
                }
            } else if (table[index] != null
                    && (table[index].key == key || table[index].key.equals(key))) {
                table[index].value = value;
            } else if (table[index] != null) {
                Node<K, V> oldNode = table[index];
                while (oldNode.next != null) {
                    oldNode = oldNode.next;
                }
                oldNode.setNext(newNode);
                if (++size > threshold) {
                    resize();
                }
            } else {
                table[index] = newNode;
                if (++size > threshold) {
                    resize();
                }
            }
        }
    }

    private void putForNullKeys(V value) {
        Node<K, V> nullNode = new Node<>(0, null, value, null);
        Node<K, V> oldNode = table[0];
        if (oldNode == null) {
            table[0] = nullNode;
            if (++size > threshold) {
                resize();
            }
        } else if (oldNode.key == null) {
            oldNode.value = value;
        } else {
            while (oldNode.next != null) {
                if (oldNode.key == null) {
                    break;
                }
                oldNode = oldNode.next;
            }
            if (oldNode.key == null) {
                oldNode.value = value;
            } else {
                oldNode.setNext(nullNode);
                if (++size > threshold) {
                    resize();
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKeys();
        }
        int index = indexForPosition(findingHash(key.hashCode()), table.length);
        if (table[index] != null && (table[index].key == key || table[index].key.equals(key))) {
            return table[index].value;
        } else if (table[index] != null) {
            Node<K, V> oldNode = table[index];
            while (oldNode.next != null) {
                if (oldNode.key != null && (oldNode.key == key || oldNode.key.equals(key))) {
                    return oldNode.value;
                }
                oldNode = oldNode.next;
            }
            return oldNode.value;
        }
        return null;
    }

    private V getForNullKeys() {
        Node<K, V> oldNode = table[0];
        if (oldNode.key != null) {
            while (oldNode.next != null && oldNode.key != null) {
                oldNode = oldNode.next;
            }
        }
        return oldNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int newThr;
        Node<K, V>[] oldTab;
        oldTab = table;
        newThr = threshold * 2;
        table = new Node[table.length * 2];
        threshold = newThr;
        for (Node<K, V> currentNode : oldTab) {
            if (currentNode != null) {
                Node<K, V> oldNode = currentNode;
                while (oldNode.next != null) {
                    put(oldNode.next.key, oldNode.next.value);
                    oldNode = oldNode.next;
                }
                put(currentNode.key, currentNode.value);
            }
        }
    }

    static int indexForPosition(int h, int length) {
        return h & (length - 1);
    }

    static int findingHash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
