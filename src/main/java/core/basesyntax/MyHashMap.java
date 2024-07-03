package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    private class Node<K, V> {
        private int hashCode;
        private K kay;
        private V value;
        private Node<K, V> next;
        public Node(int hashCode, K kay, V value, Node<K, V> next) {
            this.hashCode = hashCode;
            this.kay = kay;
            this.value = value;
            this.next = next;
        }

    }
}
