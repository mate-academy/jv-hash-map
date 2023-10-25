package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = (table.length - 1) & hash(key);
        Node<K, V> tempNode = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        } else {
            while (tempNode != null) {
                if (tempNode.key == null ? key == null : tempNode.key.equals(key)) {
                    tempNode.value = value;
                    return;
                }
                if (tempNode.next == null) {
                    tempNode.next = new Node<>(hash(key), key, value, null);
                    break;
                }
                tempNode = tempNode.next;
            }
        }

        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = (table.length - 1) & hash(key);
        Node<K, V> temp = table[index];
        while (temp != null) {
            if (temp.key == null ? key == null : temp.key.equals(key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
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

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        final Node<K, V>[] oldTab = table;
        final int oldCap = table == null ? 0 : table.length;
        int newCap = oldCap << 1;
        threshold = threshold << 1;
        table = (Node<K, V>[]) new Node[newCap];
        transferNode(oldTab, oldCap);
    }

    private void transferNode(Node<K, V>[] oldTab, int oldCap) {
        for (int i = 0; i < oldTab.length; i++) {
            Node<K, V> tempNode = oldTab[i];
            if (tempNode != null) {
                if (tempNode.next == null) {
                    table[(table.length - 1) & tempNode.hash] = tempNode;
                } else {
                    Node<K,V> loHead = null;
                    Node<K,V> loTail = null;
                    Node<K,V> hiHead = null;
                    Node<K,V> hiTail = null;
                    Node<K,V> next;
                    do {
                        next = tempNode.next;
                        if ((tempNode.hash & oldCap) == 0) {
                            if (loTail == null) {
                                loHead = tempNode;
                            } else {
                                loTail.next = tempNode;
                            }
                            loTail = tempNode;
                        } else {
                            if (hiTail == null) {
                                hiHead = tempNode;
                            } else {
                                hiTail.next = tempNode;
                            }
                            hiTail = tempNode;
                        }
                    } while ((tempNode = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        table[i] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        table[(table.length - 1) & hiHead.hash] = hiHead;
                    }
                }
            }
        }
    }
}
