package core.basesyntax;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    int size = 0;
    @SuppressWarnings("unchecked")
    Node<K,V>[] nodes = new Node[DEFAULT_INITIAL_CAPACITY];

    static class Node<K,V> {
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

        public final K getKey() { return key; }

        public final V getValue() { return value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }

    @Override
    public void put(K key, V value) {
        putPrivate(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        int nodeNumber = hash == 0 ? 0 : hash % nodes.length;

        if (nodes[nodeNumber] == null) { return null; }
        if (nodes[nodeNumber].getKey() == null
                || nodes[nodeNumber].getKey().equals(key)) {
            return nodes[nodeNumber].getValue();
        }
        Node<K,V> checkNext = nodes[nodeNumber];
        while (checkNext != null) {
            checkNext = checkNext.next;
            if (checkNext.key == key
                    || (checkNext.getKey() != null && checkNext.getKey().equals(key))) {
                return checkNext.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putPrivate (K key, V value) {
        int hash = key == null ? 0 : Math.abs(key.hashCode());
        int nodeNumber = hash == 0 ? 0 : hash % nodes.length;
        if (nodes[nodeNumber] == null) {
            nodes[nodeNumber] =  new Node<>(hash, key, value, null);
            size++;
            resize();
        } else {
            Node<K,V> checkNext = nodes[nodeNumber];
            while (checkNext != null) {
                if ((checkNext.key == null && key == null)
                        || (checkNext.key != null && checkNext.key.equals(key))) {
                    checkNext.value = value;
                    break;
                }
                if (checkNext.next == null) {
                    checkNext.next = new Node<>(hash, key, value, null);
                    size++;
                    resize();
                    break;
                }
                checkNext = checkNext.next;
            }
        }
    }

    private void resize() {
        if (size > nodes.length * DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] nodesCopy = nodes;
            nodes = new Node[nodes.length * 2];
            size = 0;
            for (Node<K,V> i : nodesCopy) {
                if (i == null) {
                    continue;
                }
                putPrivate(i.key, i.value);
                Node<K,V> checkNext = i;
                while (checkNext.next != null) {
                    checkNext = checkNext.next;
                    putPrivate(checkNext.key, checkNext.value);
                }
            }
        }
    }
}
