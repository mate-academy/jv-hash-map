package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int capasitu = 16;
    private float loadFactor = 0.75f;
    private int size;
    private Node<K,V>[] table;

    private class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value,Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        size = 0;
        table = new Node[capasitu];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * loadFactor) {
            resize();
        }
        Node<K,V> node = table[hash(key) % table.length];
        if (node == null) {
            table[hash(key) % table.length] = new Node<>(key,value,null);
            size++;
        } else {
            while (node != null) {

                if (node.next == null) {
                    if (node.key == null) {
                        if (key == null) {
                            node.value = value;
                            break;
                        }
                    } else if (node.key.equals(key)) {
                        node.value = value;
                        break;
                    }
                    node.next = new Node<>(key,value,null);
                    size++;
                    break;
                } else if (node.key == null) {
                    if (key == null) {
                        node.value = value;
                        break;
                    }
                } else if (node.key.equals(key)) {
                    node.value = value;
                    break;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[hash(key) % table.length];
        if (node != null) {
            while (node != null) {
                if (node.key != null) {
                    if (node.key.equals(key)) {
                        return node.value;
                    }
                } else {
                    if (key == null) {
                        return node.value;
                    }
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capasitu);
    }

    private void resize() {
        capasitu = capasitu * 2;
        Node<K, V>[] newTable = new Node[capasitu];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = hash(node.key);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
    }

}
