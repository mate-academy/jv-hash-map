package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    //private static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int currentLength = table.length;
    private int size;
    private Node<K, V> node;
    Node<K, V> last = null;
    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        int index = Node.hashCode(key);
        index = index >= 1 ? index : index * -1;
        node = new Node<>(index, key, value, null);
        Node<K, V> node1 = table[index];
        if (node.hash == 0) {
            if (table[0] == null) {
                table[0] = node;
                size++;
            } else {
                table[0] = node;
            }
            return;
        }
        index = index % currentLength;
        if (table[index] != null) {
            if (!table[index].key.equals(node.key)) {
                if (table[index].next == null) {
                    table[index].next = node;
                    last = node;
                } else {
                    last.next = node;
                    last = node;
                }
            } else {
                table[index].value = node.value;
                return;
            }
        } else {
            table[index] = node;
        }
        size++;
    }


    @Override
    public V getValue(K key) {
        int index = Node.hashCode(key) >= 1 ? Node.hashCode(key) : Node.hashCode(key) * -1;
        index = index % currentLength;
        Node<K,V> node = table[index];
        if (node != null && node.next == null) {
            return node.value;
        } else {
            while (node != null && node != null) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
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
    public void resizeIfNeed() {
        if (size >= currentLength * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] newTab = new Node[currentLength * 2];
            Node<K,V>[] oldTab = table;
            table = newTab;
            currentLength *= 2;
            for (Node<K,V> nd : oldTab){
                if (nd != null) {
                    while (nd != null) {
                        //System.out.println(nd.key + " "  + nd.value);
                        put(nd.key, nd.value);
                        nd = nd.next;
                        size--;
                    }
                }
            }
        }
    }

    static class Node<K, V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
    public static int hashCode(Object key) {
        return (key == null) ? 0 : key.hashCode() * 17 + 1;
    }
    public final String toString() { return key + "=" + value; }
    }
}
